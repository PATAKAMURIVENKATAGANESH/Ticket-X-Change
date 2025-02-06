package com.TicketXChange.TicketXChange.ticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelSearchRequest {
    private String city;
    private String hotelName;
    private String roomType;
    private boolean acNac; // AC or NAC
    private int numberOfRooms;
    private int numberOfDays;
    private String checkInTime;
    private String checkOutTime;
}
