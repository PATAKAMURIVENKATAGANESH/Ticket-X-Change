package com.TicketXChange.TicketXChange.ticket.repository;

import com.TicketXChange.TicketXChange.ticket.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByOrderId(String orderId);
}
