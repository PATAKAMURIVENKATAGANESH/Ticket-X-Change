package com.TicketXChange.TicketXChange.ticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieSellerRequest {
    private String city;
    private String movie;
    private String theatre;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private int numberOfTickets;
    private List<String> seats;
}
