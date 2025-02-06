package com.TicketXChange.TicketXChange.ticket.controller;

//import com.TicketXChange.TicketXChange.ticket.dto.TicketBuyRequest;
//import com.TicketXChange.TicketXChange.ticket.dto.TicketBuyResponse;
import com.TicketXChange.TicketXChange.auth.exception.UserNotFoundException;
import com.TicketXChange.TicketXChange.auth.model.User;
import com.TicketXChange.TicketXChange.auth.repository.UserProfileRepository;
import com.TicketXChange.TicketXChange.ticket.dtos.PaymentSuccessRequest;
import com.TicketXChange.TicketXChange.ticket.dtos.TicketBuyResponse;
import com.TicketXChange.TicketXChange.ticket.service.OrderService;
import com.TicketXChange.TicketXChange.ticket.service.PaymentService;
//import com.TicketXChange.TicketXChange.ticket.service.TicketBuyService;
import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/buy-ticket")
@RequiredArgsConstructor
public class TicketBuyController {

//    private final TicketBuyService ticketBuyService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final UserProfileRepository userProfileRepository;

//    @PostMapping("/buy")
//    public ResponseEntity<TicketBuyResponse> buyTicket(@RequestParam Integer id) {
//        // For simplicity, we assume UserProfile is fetched from security context or another service.
//      UserProfile userProfile = getCurrentUser();
//
//        TicketBuyResponse response = ticketBuyService.buyTicket(userProfile, id);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestParam Integer id) {
        try {
            UserProfile user = getCurrentUser();
            String paymentUrl = orderService.createOrder(user, id);
            return ResponseEntity.ok(paymentUrl);
        } catch (RazorpayException e) {
            return ResponseEntity.status(500).body("Error creating order: " + e.getMessage());
        }
    }



    public UserProfile getCurrentUser(){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = currentUser.getUsername();

        // Retrieve the user's profile from the repository
        Optional<UserProfile> userProfileOptional = userProfileRepository.findByEmail(userEmail);

        if (!userProfileOptional.isPresent()) {
            throw new UserNotFoundException("user does not exist");
        }

        UserProfile userProfile = userProfileOptional.get();
        return userProfile;
    }
}
