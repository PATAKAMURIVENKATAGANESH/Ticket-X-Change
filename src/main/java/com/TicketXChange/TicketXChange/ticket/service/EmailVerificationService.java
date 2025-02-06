package com.TicketXChange.TicketXChange.ticket.service;

import com.TicketXChange.TicketXChange.chatgptverify.controller.CustomBotController;
import com.TicketXChange.TicketXChange.chatgptverify.dto.UploadRequest;
import com.TicketXChange.TicketXChange.ticket.model.MailVerification;
import com.TicketXChange.TicketXChange.ticket.model.Movie;
import com.TicketXChange.TicketXChange.ticket.model.Ticket;
import com.TicketXChange.TicketXChange.ticket.repository.MailVerificationRepository;
import com.TicketXChange.TicketXChange.ticket.repository.MovieRepository;
import com.TicketXChange.TicketXChange.ticket.enums.VerificationStatus;
import com.TicketXChange.TicketXChange.ticket.enums.VerificationTag;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.search.HeaderTerm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.mail.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    private final MailVerificationRepository mailVerificationRepository;
    private final MovieRepository movieRepository;
    private final CustomBotController customBotController;

    public void verifyEmail(Ticket ticket, MailVerification mailVerification) throws Exception {
        String mailNumber = mailVerification.getMailNumber();

        // Retrieve the email by its mail number
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.ssl.trust", "*");
        properties.put("mail.imaps.ssl.protocols", "TLSv1.2");

        Session emailSession = Session.getDefaultInstance(properties);
        Store store = emailSession.getStore("imaps");
        store.connect("imap.gmail.com", username, password);

        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY); // Open folder for read only

        mailNumber = "<"+ mailNumber.substring(1, mailNumber.length() - 1) + ">";

        Message[] messages = emailFolder.search(new HeaderTerm("Message-ID", mailNumber));
        System.out.println(messages.length);
        if (messages.length > 0) {
            Message message = messages[0];

            // Extract the email body
            String emailBody = getTextFromMessage(message);
            System.out.println("email body "+emailBody);
            String bookingDetailsBody = "parse the below text and map and give me, theatre name, date and time, seats, location: format should like only and other explaining text should be there-> key:theatre name, value:the extracted value -> like below :{\"theatre name\": \"Sai Krishna 4K Dolby Atmos Ultra HD 3D(SAI KRISHNA THEATRE)\", \"date and time\": \"Wed, 25 Oct, 2023 | 02:30pm\", \"seats\": \"LOWER BA - H7, H8, H9\",\n" + "}" + emailBody;
            UploadRequest uploadRequest = UploadRequest.builder().description(bookingDetailsBody).build();
            String ticketMapping = customBotController.chat(uploadRequest);
            System.out.println("ticket mapping "+ticketMapping);
            System.out.println(bookingDetailsBody);
            // Parsing the returned booking details string into a Map
            Map<String, String> bookingDetails = parseBookingDetails(ticketMapping);
            System.out.println("booking details "+bookingDetails);
            if (bookingDetails != null) {
                // Fetch movie details associated with the ticket
                Optional<Movie> optionalMovie = movieRepository.findByTicket(ticket);
                if (optionalMovie.isPresent()) {
                    Movie movie = optionalMovie.get();

                    // Match booking details
                    boolean isMatching =
                            bookingDetails.getOrDefault("theatre name", "").equalsIgnoreCase(movie.getTheatre()) &&
                                    bookingDetails.getOrDefault("location", "").equalsIgnoreCase(movie.getCity()) &&
                                    compareDates(bookingDetails.get("date and time"), movie.getFromDate());

                    if (isMatching) {
                        mailVerification.setStatus(VerificationStatus.APPROVED);
                        ticket.setTag(VerificationTag.VERIFIED);
                    } else {
                        mailVerification.setStatus(VerificationStatus.REJECTED);
                    }
                } else {
                    mailVerification.setStatus(VerificationStatus.REJECTED);
                }
                mailVerificationRepository.save(mailVerification);
            }
        }

        emailFolder.close(false);
        store.close();
    }

    private String getTextFromMessage(Message message) throws Exception {

        return new BufferedReader(new InputStreamReader(message.getInputStream()))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private boolean compareDates(String bookingDateStr, LocalDateTime movieDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM, yyyy | hh:mma");
        LocalDateTime bookingDateTime = LocalDateTime.parse(bookingDateStr, formatter);
        return bookingDateTime.equals(movieDateTime);
    }

    private Map<String, String> parseBookingDetails(String bookingDetails) {
        Map<String, String> detailsMap = new HashMap<>();
        try {
            String jsonContent = bookingDetails.substring(bookingDetails.indexOf('{'), bookingDetails.lastIndexOf('}') + 1);
            ObjectMapper objectMapper = new ObjectMapper();
            detailsMap = objectMapper.readValue(bookingDetails, HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detailsMap;
    }
}