package com.TicketXChange.TicketXChange.payment.dto;

import lombok.Data;

@Data
public class RazorpayOrder {
    private String id;
    private String amount;
    private String currency;
    // Add other fields as needed
}

