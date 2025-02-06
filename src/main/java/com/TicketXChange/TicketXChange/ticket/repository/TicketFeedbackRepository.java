package com.TicketXChange.TicketXChange.ticket.repository;

import com.TicketXChange.TicketXChange.ticket.model.TicketFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketFeedbackRepository extends JpaRepository<TicketFeedback, Long> {
}
