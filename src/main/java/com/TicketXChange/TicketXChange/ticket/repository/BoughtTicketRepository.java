package com.TicketXChange.TicketXChange.ticket.repository;

import com.TicketXChange.TicketXChange.ticket.model.BoughtTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoughtTicketRepository extends JpaRepository<BoughtTicket, Long> {
}
