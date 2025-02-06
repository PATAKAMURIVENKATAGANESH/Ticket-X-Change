package com.TicketXChange.TicketXChange.ticket.repository;

import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.ticket.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findTopByUserOrderByCreatedAtDesc(UserProfile userProfile);
    // Additional query methods (if needed) can be defined here
}
