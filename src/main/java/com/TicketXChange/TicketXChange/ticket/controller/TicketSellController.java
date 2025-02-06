package com.TicketXChange.TicketXChange.ticket.controller;

import com.TicketXChange.TicketXChange.auth.exception.UserNotFoundException;
import com.TicketXChange.TicketXChange.auth.model.User;
import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.auth.repository.UserProfileRepository;
import com.TicketXChange.TicketXChange.ticket.dtos.MovieSellerRequest;
import com.TicketXChange.TicketXChange.ticket.dtos.TicketResponse;
import com.TicketXChange.TicketXChange.ticket.service.TicketSellService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/submit-sell-request")
@AllArgsConstructor
public class TicketSellController {

    private final UserProfileRepository userProfileRepository;
    private final TicketSellService ticketSellService;

    @PostMapping("/movie")
    public String submitMovieSellRequest(@RequestBody MovieSellerRequest request) {

        UserProfile userProfile = getCurrentUser();

        TicketResponse ticketResponse = ticketSellService.sellMovieRequest(userProfile, request);

        return "Movie ticket sell request submitted successfully.";
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
