package com.TicketXChange.TicketXChange.ticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketBuyResponse {
    private boolean success;
    private String message;
    private String error;
}
