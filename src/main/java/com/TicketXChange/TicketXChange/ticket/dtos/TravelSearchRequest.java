package com.TicketXChange.TicketXChange.ticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelSearchRequest {
    private String travelType; // BUS, TRAIN, FLIGHT
    private String startLocation;
    private String destinationLocation;
    private String date;
    private int numberOfTickets;
}
