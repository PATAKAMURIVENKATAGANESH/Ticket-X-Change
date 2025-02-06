//package com.TicketXChange.TicketXChange.ticket.service;
//
//import com.TicketXChange.TicketXChange.auth.model.UserProfile;
//import com.TicketXChange.TicketXChange.auth.service.EmailService;
//import com.TicketXChange.TicketXChange.payment.dto.RazorpayOrder;
//import com.TicketXChange.TicketXChange.payment.model.Payment;
//import com.TicketXChange.TicketXChange.payment.model.PaymentToSellersByPlatform;
//import com.TicketXChange.TicketXChange.payment.repository.PaymentRepository;
//import com.TicketXChange.TicketXChange.payment.repository.PaymentToSellersByPlatformRepository;
//import com.TicketXChange.TicketXChange.payment.service.RazorpayService;
//import com.TicketXChange.TicketXChange.ticket.dtos.TicketBuyResponse;
//import com.TicketXChange.TicketXChange.ticket.enums.AvailabilityStatus;
//import com.TicketXChange.TicketXChange.payment.enums.PaidByPlatformStatus;
//import com.TicketXChange.TicketXChange.payment.enums.PaymentStatus;
//import com.TicketXChange.TicketXChange.ticket.model.*;
//import com.TicketXChange.TicketXChange.ticket.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class TicketBuyService {
//    private final RazorpayService razorpayService;
//    private final TicketRepository ticketRepository;
//    private final PaymentRepository paymentRepository;
//    private final PaymentToSellersByPlatformRepository paymentToSellersByPlatformRepository;
//    private final BoughtTicketRepository boughtTicketsRepository;
//    private final TicketFeedbackRepository ticketFeedbackRepository;
//    private final EmailService emailService;
//    private final MailVerificationRepository mailVerificationRepository;
//
//    @Transactional
//    public TicketBuyResponse buyTicket(UserProfile userProfile, int ticketId) {
//        // Validate user profile and ticket ID
//        Optional<Ticket> optionalTicket = ticketRepository.findById((long) ticketId);
//        if (optionalTicket.isEmpty()) {
//            throw new RuntimeException("Ticket not found");
//        }
//
//        Ticket ticket = optionalTicket.get();
//
//        String ticketUrl = "";
//        Optional<MailVerification> optionalMailTicket = mailVerificationRepository.findByTicket(ticket);
//        MailVerification mailTicket;
//        if(optionalMailTicket.isPresent()){
//            mailTicket = optionalMailTicket.get();
//            ticketUrl = mailTicket.getTicketUrl();
//        }
//        else{
//            return TicketBuyResponse.builder()
//                    .success(true)
//                    .error("couldn't inititate the next steps")
//                    .message("the ticket email is not forwarded by owner")
//                    .build();
//        }
//
//        // Perform payment using Razorpay API
////        try {
////            int amountInPaise = 100; // Replace with actual amount
////            String currency = "INR";
////            String email = userProfile.getEmail();
////
////            RazorpayOrder order = razorpayService.createOrder(email, amountInPaise, currency);
////
////            if (razorpayService.isPaymentSuccessful(order.getId())) {
////                // Create payment record
////                Payment payment = Payment
////                        .builder()
////                        .status(PaymentStatus.SUCCESS)
////                        .transactionId(order.getId())
////                        .buyer(userProfile)
////                        .seller(ticket.getUser())
////                        .ticket(ticket)
////                        .build();
////
////                paymentRepository.save(payment);
//
//                // Mark the ticket as sold
////                ticket.setAvailabilityStatus(AvailabilityStatus.SOLD);
////                ticketRepository.save(ticket);
//
//                // Create entry in Payment-to-sellers-by-platform table
////                PaymentToSellersByPlatform paymentToSellersByPlatform = new PaymentToSellersByPlatform();
////                paymentToSellersByPlatform.setPaymentId(payment.getId());
////                paymentToSellersByPlatform.setUser(ticket.getUser());
////                paymentToSellersByPlatform.setPaidByPlatformStatus(PaidByPlatformStatus.TO_BE_RECEIVED);
////                paymentToSellersByPlatform.setTicket(ticket);
////                paymentToSellersByPlatformRepository.save(paymentToSellersByPlatform);
////
////                // Create entry in BoughtTickets table
////                BoughtTicket boughtTicket = new BoughtTicket();
////                boughtTicket.setBuyer(userProfile);
////                boughtTicket.setPaymentId(payment.getId());
////                boughtTicket.setCreatedAt(LocalDateTime.now());
////                boughtTicket.setTicket(ticket);
////                boughtTicketsRepository.save(boughtTicket);
//
//                // Send ticket URL to buyer's email
//
//                 // Replace with actual URL
////                EmailUtils emailUtils = new EmailUtils();
////                emailUtils.forwardEmail(mailTicket.getMailNumber(), userProfile.getEmail());
//                // Return response
//                return TicketBuyResponse.builder()
//                        .success(true)
//                        .message("ticket successfully transferred to your mail")
//                        .build();
////            } else {
////                throw new RuntimeException("Payment failed");
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////            throw new RuntimeException("Error processing payment");
////        }
//    }
//}
