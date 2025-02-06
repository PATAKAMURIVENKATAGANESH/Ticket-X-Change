package com.TicketXChange.TicketXChange.ticket.repository;

import com.TicketXChange.TicketXChange.ticket.model.Movie;
import com.TicketXChange.TicketXChange.ticket.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTicket(Ticket ticket);
    // Additional query methods (if needed) can be defined here
//    List<Movie> findByCityAndMovieAndTheatreAndFromDate(String city, String movie, String theatre, LocalDateTime fromDate);
    @Query("SELECT m FROM Movie m WHERE m.city = :city AND m.movie = :movie AND m.theatre = :theatre AND CAST(m.fromDate AS date) = :date")
    List<Movie> findByCityAndMovieAndTheatreAndFromDate(
            @Param("city") String city,
            @Param("movie") String movie,
            @Param("theatre") String theatre,
            @Param("date") LocalDate date);
}
