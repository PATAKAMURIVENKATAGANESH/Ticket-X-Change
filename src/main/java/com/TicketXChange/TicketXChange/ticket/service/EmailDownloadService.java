package com.TicketXChange.TicketXChange.ticket.service;

import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.auth.repository.UserProfileRepository;
import com.TicketXChange.TicketXChange.auth.service.S3FileService;
import com.TicketXChange.TicketXChange.ticket.enums.VerificationStatus;
import com.TicketXChange.TicketXChange.ticket.model.MailVerification;
import com.TicketXChange.TicketXChange.ticket.model.Ticket;
import com.TicketXChange.TicketXChange.ticket.repository.MailVerificationRepository;
import com.TicketXChange.TicketXChange.ticket.repository.TicketRepository;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.FlagTerm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmailDownloadService {

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.curl.ca.bundle}")
    private String curlCaBundle;

    private final MailVerificationRepository mailVerificationRepository;
    private final S3FileService s3FileService;
    private final UserProfileRepository userProfileRepository;
    private final TicketRepository ticketRepository;
    private final EmailVerificationService emailVerificationService;
//    @Scheduled(fixedRate = 60000) // Execute every minute
    public void downloadUnreadEmails() {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.ssl.trust", "*");
        properties.put("mail.imaps.ssl.protocols", "TLSv1.2");

        try {
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect("imap.gmail.com", username, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE); // Open folder for read and write

            Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            for (Message message : messages) {
                String sMail = sanitizeFilename(((MimeMessage) message).getFrom()[0].toString());
                String senderMail = extractContentBetweenUnderscores(sMail);
                String originalSenderMail = sanitizeFilename(extractOriginalSender(message));
                String mailNumber = sanitizeFilename(message.getHeader("Message-ID")[0]);
                LocalDateTime timestamp = LocalDateTime.now();
                System.out.println(senderMail);

                UserProfile userProfile = userProfileRepository.findByEmail(senderMail)
                        .orElseThrow(() -> new RuntimeException("User not found with email: " + senderMail));

                // Fetch the most recently submitted ticket request
                Ticket ticket = ticketRepository.findTopByUserOrderByCreatedAtDesc(userProfile)
                        .orElseThrow(() -> new RuntimeException("No ticket found for user: " + senderMail));


                MailVerification mailVerification = new MailVerification();
                mailVerification.setOriginalOwnerMail(originalSenderMail);


                mailVerification.setOwnerMail(senderMail);
                mailVerification.setMailNumber(mailNumber);
                mailVerification.setStatus(VerificationStatus.PENDING);
                mailVerification.setTicket(ticket);

                // Generate unique S3 number (example: combination of mailNumber and senderMail)
                String uniqueS3Number = mailNumber + "_" + senderMail;

                saveEmailToFile(message, originalSenderMail, senderMail, mailNumber);
//                String ticketUrl = uploadEmailToS3(message, uniqueS3Number);
                String ticketUrl = "";

                mailVerification.setTicketUrl(ticketUrl);

                mailVerificationRepository.save(mailVerification);

                // Mark email as read (SEEN)
                message.setFlag(Flags.Flag.SEEN, true);
                emailVerificationService.verifyEmail(ticket, mailVerification);
            }

            emailFolder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String extractContentBetweenUnderscores(String input) {
        try {
            int firstUnderscoreIndex = input.indexOf('_');
            int secondUnderscoreIndex = input.indexOf('_', firstUnderscoreIndex + 1);

            if (firstUnderscoreIndex != -1 && secondUnderscoreIndex != -1) {
                return input.substring(firstUnderscoreIndex + 1, secondUnderscoreIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if underscores are not found or any error occurs
    }

    private String extractOriginalSender(Message message) throws IOException, MessagingException {
        if (message.isMimeType("text/plain")) {
            String body = (String) message.getContent();
            return extractOriginalSenderFromBody(body);
        } else if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    String body = (String) bodyPart.getContent();
                    return extractOriginalSenderFromBody(body);
                }
            }
        }
        return "unknown_sender";
    }

    private String extractOriginalSenderFromBody(String body) {
        Pattern pattern = Pattern.compile("From: .* <(.*)>");
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "unknown_sender";
    }

    private void saveEmailToFile(Message message, String originalSender, String sender, String mailNumber) throws IOException, MessagingException {
        Path path = Paths.get(originalSender + "_" + sender + "_" + mailNumber + ".eml");
        try (InputStream is = message.getInputStream();
             FileOutputStream fos = new FileOutputStream(path.toFile())) {
            byte[] buf = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buf)) != -1) {
                fos.write(buf, 0, bytesRead);
            }
        }
    }

    private String uploadEmailToS3(Message message, String uniqueS3Number) throws MessagingException, IOException {
        String filename = uniqueS3Number + ".eml";
        byte[] emailBytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            message.writeTo(baos);
            emailBytes = baos.toByteArray();
        }
        s3FileService.uploadFile(filename, emailBytes, "eml");
        return s3FileService.getObjectUrl(filename);
    }
    private String sanitizeFilename(String filename) {
        return filename.replaceAll("[\\\\/:*?\"<>|]", "_");
    }
}
