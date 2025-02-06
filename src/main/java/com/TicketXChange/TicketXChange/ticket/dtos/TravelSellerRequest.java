package com.TicketXChange.TicketXChange.ticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelSellerRequest {
    private String travelType; // BUS, TRAIN, FLIGHT
    private String startLocation;
    private String destinationLocation;
    private String date;
    private int numberOfTickets;
    private String[] seatNumbers;
    private String time;
}
