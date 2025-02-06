package com.TicketXChange.TicketXChange.ticket.controller;

import com.TicketXChange.TicketXChange.auth.exception.UserNotFoundException;
import com.TicketXChange.TicketXChange.auth.model.User;
import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.auth.repository.UserProfileRepository;
import com.TicketXChange.TicketXChange.ticket.dtos.MovieSearchRequest;
import com.TicketXChange.TicketXChange.ticket.dtos.TicketSearchResponse;
import com.TicketXChange.TicketXChange.ticket.service.TicketSearchService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/search-ticket")
@AllArgsConstructor
public class TicketSearchController {

    private final UserProfileRepository userProfileRepository;
    private final TicketSearchService ticketBuyService;

    @PostMapping("/movie")
    public TicketSearchResponse submitMovieSellRequest(@RequestBody MovieSearchRequest request) {

        UserProfile userProfile = getCurrentUser();

        TicketSearchResponse ticketResponse = ticketBuyService.searchMovieRequest(userProfile, request);

        return ticketResponse;
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
