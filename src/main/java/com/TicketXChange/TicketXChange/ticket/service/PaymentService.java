package com.TicketXChange.TicketXChange.ticket.service;

import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.payment.dto.RazorpayOrder;
import com.TicketXChange.TicketXChange.payment.dto.RazorpayPayment;
import com.TicketXChange.TicketXChange.payment.enums.PaymentStatus;
import com.TicketXChange.TicketXChange.payment.model.Payment;
import com.TicketXChange.TicketXChange.payment.repository.PaymentRepository;
import com.TicketXChange.TicketXChange.payment.service.RazorpayService;
import com.TicketXChange.TicketXChange.ticket.enums.AvailabilityStatus;
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
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RazorpayService razorpayService;
    private final TicketRepository ticketRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void handlePaymentSuccess(UserProfile userProfile, String orderId, String paymentId) throws RazorpayException {
        RazorpayOrder razorpayOrder = razorpayService.getOrder(orderId);
        RazorpayPayment razorpayPayment = razorpayService.fetchPayment(paymentId);

        if ("paid".equalsIgnoreCase(razorpayPayment.getStatus())) {
            Optional<Order> optionalOrder = orderRepository.findByOrderId(orderId);
            if (optionalOrder.isEmpty()) {
                throw new RuntimeException("Order not found");
            }

            Order order = optionalOrder.get();
            Ticket ticket = order.getTicket();

            Payment payment = Payment.builder()
                    .status(PaymentStatus.SUCCESS)
                    .transactionId(paymentId)
                    .orderId(orderId)
                    .buyer(userProfile)
                    .seller(ticket.getUser())
                    .ticket(ticket)
                    .build();

            paymentRepository.save(payment);

            ticket.setAvailabilityStatus(AvailabilityStatus.SOLD);
            ticketRepository.save(ticket);
        } else {
            throw new RuntimeException("Payment failed");
        }
    }
}
