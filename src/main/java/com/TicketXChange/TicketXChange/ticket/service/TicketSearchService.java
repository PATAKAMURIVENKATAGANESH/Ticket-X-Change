package com.TicketXChange.TicketXChange.ticket.service;

import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.ticket.dtos.MovieSearchRequest;
import com.TicketXChange.TicketXChange.ticket.dtos.MovieTicketResponse;
import com.TicketXChange.TicketXChange.ticket.dtos.TicketSearchResponse;
import com.TicketXChange.TicketXChange.ticket.model.Movie;
import com.TicketXChange.TicketXChange.ticket.model.Ticket;
import com.TicketXChange.TicketXChange.ticket.repository.MovieRepository;
import com.TicketXChange.TicketXChange.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketSearchService {
    private final TicketRepository ticketRepository;
    private final MovieRepository movieRepository;

    public TicketSearchResponse searchMovieRequest(UserProfile userProfile, MovieSearchRequest request) {
        LocalDate date = request.getDate();
        System.out.println(date);
        // Call the repository method
        List<Movie> movies = movieRepository.findByCityAndMovieAndTheatreAndFromDate(
                request.getCity(), request.getMovie(), request.getTheatre(), date);
        System.out.println(movies.size());


        List<MovieTicketResponse> ticketResponses = movies.stream()
                .map(movie -> {
                    Ticket ticket = movie.getTicket();

                    return MovieTicketResponse.builder()
                            .id(movie.getId())
                            .city(movie.getCity())
                            .movie(movie.getMovie())
                            .theatre(movie.getTheatre())
                            .fromDate(movie.getFromDate().toString())
                            .toDate(movie.getToDate().toString())
                            .numberOfTickets(movie.getNumberOfTickets())
                            .seats(movie.getSeats())
                            .ticketId(ticket.getTicketId())
                            .user(ticket.getUser().getUserName())
                            .createdAt(ticket.getCreatedAt().toString())
                            .category(ticket.getCategory().toString())
                            .tag(ticket.getTag().toString())
                            .availabilityStatus(ticket.getAvailabilityStatus().toString())
                            .mailForwarded(ticket.isMailForwarded())
                            .build();
                })
                .collect(Collectors.toList());

        return TicketSearchResponse.builder()
                .tickets(ticketResponses)
                .build();
    }
}
