package com.TicketXChange.TicketXChange.ticket.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuccessRequest {

//    private String transactionId;
    private String orderId;
}
