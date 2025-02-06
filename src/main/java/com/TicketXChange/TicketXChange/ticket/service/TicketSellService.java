package com.TicketXChange.TicketXChange.ticket.service;

import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.ticket.dtos.MovieSellerRequest;
import com.TicketXChange.TicketXChange.ticket.dtos.TicketResponse;
import com.TicketXChange.TicketXChange.ticket.enums.AvailabilityStatus;
import com.TicketXChange.TicketXChange.ticket.enums.Category;
import com.TicketXChange.TicketXChange.ticket.enums.VerificationTag;
import com.TicketXChange.TicketXChange.ticket.model.Movie;
import com.TicketXChange.TicketXChange.ticket.model.Ticket;
import com.TicketXChange.TicketXChange.ticket.repository.MovieRepository;
import com.TicketXChange.TicketXChange.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketSellService {

    private final TicketRepository ticketRepository;
    private final MovieRepository movieRepository;

    public TicketResponse sellMovieRequest(UserProfile userProfile, MovieSellerRequest request) {

        // Create and save Ticket
        Ticket ticket = Ticket.builder()
                .ticketId(UUID.randomUUID().toString())
                .category(Category.MOVIE)
                .tag(VerificationTag.UNVERIFIED)
                .availabilityStatus(AvailabilityStatus.AVAILABLE)
                .mailForwarded(false)
                .user(userProfile)
                .build();
        ticketRepository.save(ticket);

        // Create and save Movie
        Movie movie = Movie.builder()
                .city(request.getCity())
                .movie(request.getMovie())
                .theatre(request.getTheatre())
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .numberOfTickets(request.getNumberOfTickets())
                .seats(request.getSeats())
                .ticket(ticket)
                .build();
        movieRepository.save(movie);

        TicketResponse ticketResponse = TicketResponse
                .builder()
                .success(true)
                .message("request has been submmitted successfully")
                .build();

        return ticketResponse;
    }
}
