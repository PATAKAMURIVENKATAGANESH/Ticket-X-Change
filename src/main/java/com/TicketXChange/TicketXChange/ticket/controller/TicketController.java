package com.TicketXChange.TicketXChange.ticket.controller;


import com.TicketXChange.TicketXChange.ticket.service.EmailDownloadService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/ticketverify")
@AllArgsConstructor
public class TicketController {
    private final EmailDownloadService emailDownloadService;

    @RequestMapping("/download")
    public String downloadTicket() throws MessagingException, IOException {
        try {
            emailDownloadService.downloadUnreadEmails();
            return "done";
        }
        catch(Exception e){
            System.out.println("Error occurred during email download: " + e.getMessage());
            throw e;  // Re-throw the exception to propagate it up to the caller.
        }

    }  // End of downloadTicket() method.

    @RequestMapping("/get-ticket-download-status")
    public String downloadTicketStatus() throws MessagingException, IOException {
        return null;
    }
}

