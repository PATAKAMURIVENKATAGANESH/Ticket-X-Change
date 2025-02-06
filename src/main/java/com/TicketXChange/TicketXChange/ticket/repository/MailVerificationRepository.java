package com.TicketXChange.TicketXChange.ticket.repository;

import com.TicketXChange.TicketXChange.ticket.model.BoughtTicket;
import com.TicketXChange.TicketXChange.ticket.model.MailVerification;
import com.TicketXChange.TicketXChange.ticket.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailVerificationRepository extends JpaRepository<MailVerification, Long> {
    Optional<MailVerification> findByTicket(Ticket boughtTicket);
    // Additional query methods (if needed) can be defined here
}
