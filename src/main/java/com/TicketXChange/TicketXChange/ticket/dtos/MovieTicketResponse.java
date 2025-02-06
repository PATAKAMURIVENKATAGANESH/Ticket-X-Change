package com.TicketXChange.TicketXChange.ticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieTicketResponse {
    private Long id;
    private String city;
    private String movie;
    private String theatre;
    private String fromDate;
    private String toDate;
    private int numberOfTickets;
    private List<String> seats;
    private String ticketId;
    private String user;
    private String createdAt;
    private String category;
    private String tag;
    private String availabilityStatus;
    private boolean mailForwarded;
}
