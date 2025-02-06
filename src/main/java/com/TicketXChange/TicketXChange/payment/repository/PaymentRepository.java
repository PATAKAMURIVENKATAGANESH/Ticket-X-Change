package com.TicketXChange.TicketXChange.payment.repository;

import com.TicketXChange.TicketXChange.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
