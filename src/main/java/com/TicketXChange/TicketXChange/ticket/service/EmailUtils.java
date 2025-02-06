package com.TicketXChange.TicketXChange.ticket.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.HeaderTerm;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EmailUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;


    public static String extractEmailBody(String content) throws Exception {
//        InputStream source = new FileInputStream(filePath);
//        MimeMessage message = new MimeMessage(null, source);
//
//        String content = null;
//        if (message.isMimeType("multipart/*")) {
//            Multipart multipart = (Multipart) message.getContent();
//            for (int i = 0; i < multipart.getCount(); i++) {
//                BodyPart bodyPart = multipart.getBodyPart(i);
//                if (bodyPart.isMimeType("text/html")) {
//                    content = bodyPart.getContent().toString();
//                    break;
//                }
//            }
//        } else if (message.isMimeType("text/html")) {
//            content = message.getContent().toString();
//        }
//
//        if (content != null) {
            return formatExtractedLines(content, 70, 580);
//        }

//        return null;
    }

    private static String formatExtractedLines(String content, int startLine, int endLine) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(content));
        StringBuilder formattedContent = new StringBuilder();

        int lineNumber = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            if (lineNumber >= startLine && lineNumber <= endLine) {
                formattedContent.append(line).append("\\n");
            }
        }

        return formattedContent.toString();
    }

    public void forwardEmail(String mailNumber, String forwardToEmail) {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.ssl.trust", "*");
        properties.put("mail.imaps.ssl.protocols", "TLSv1.2");

        try {
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect("imap.gmail.com", username, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);

            Message[] messages = emailFolder.search(new HeaderTerm("Message-ID", mailNumber));

            if (messages.length > 0) {
                Message messageToForward = messages[0];
                forwardMessage(messageToForward, forwardToEmail);
            } else {
                System.out.println("No email found with the given mail number: " + mailNumber);
            }

            emailFolder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void forwardMessage(Message originalMessage, String forwardToEmail) throws MessagingException, IOException {
        MimeMessage forward = new MimeMessage((Session) null);

        forward.setRecipients(Message.RecipientType.TO, forwardToEmail);
        forward.setSubject("Fwd: " + originalMessage.getSubject());
        forward.setFrom(new InternetAddress(username));

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText("Forwarded message:\n\n");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(originalMessage.getDataHandler());

        multipart.addBodyPart(messageBodyPart);

        forward.setContent(multipart);

        // Send the forwarded email using JavaMailSender
        MimeMessageHelper helper = new MimeMessageHelper(forward, true);
        helper.setTo(forwardToEmail);
        helper.setSubject(forward.getSubject());
        helper.setText((String) messageBodyPart.getContent(), true);

        javaMailSender.send(forward);
        System.out.println("Email forwarded to: " + forwardToEmail);
    }
}
