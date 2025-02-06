package com.TicketXChange.TicketXChange.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RazorpayPayment {

    private String id;
    private int amount;
    private String status;
}
