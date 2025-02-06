package com.TicketXChange.TicketXChange.ticket.repository;

import com.TicketXChange.TicketXChange.ticket.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    // Additional query methods (if needed) can be defined here
}
