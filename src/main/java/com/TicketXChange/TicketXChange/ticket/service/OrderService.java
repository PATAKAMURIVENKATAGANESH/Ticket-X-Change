package com.TicketXChange.TicketXChange.ticket.service;

import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.payment.dto.RazorpayOrder;
import com.TicketXChange.TicketXChange.payment.service.RazorpayService;
import com.TicketXChange.TicketXChange.ticket.model.Order;
import com.TicketXChange.TicketXChange.ticket.model.Ticket;
import com.TicketXChange.TicketXChange.ticket.repository.OrderRepository;
import com.TicketXChange.TicketXChange.ticket.repository.TicketRepository;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RazorpayService razorpayService;
    private final TicketRepository ticketRepository;

    @Transactional
    public String createOrder(UserProfile userProfile, int ticketId) throws RazorpayException {
        Optional<Ticket> optionalTicket = ticketRepository.findById((long) ticketId);
        if (optionalTicket.isEmpty()) {
            throw new RuntimeException("Ticket not found");
        }

        Ticket ticket = optionalTicket.get();
        int amountInPaise = 100; // Replace with actual amount
        String currency = "INR";
        String email = userProfile.getEmail();

        RazorpayOrder razorpayOrder = razorpayService.createOrder(email, amountInPaise, currency);

        Order order = Order.builder()
                .ticket(ticket)
                .user(userProfile)
                .paymentUrl("https://checkout.razorpay.com/v1/checkout.js?order_id=" + razorpayOrder.getId())
                .orderId(razorpayOrder.getId())
                .build();

        orderRepository.save(order);
        return order.getPaymentUrl();
    }
}
