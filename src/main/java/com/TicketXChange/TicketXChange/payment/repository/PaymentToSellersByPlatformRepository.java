package com.TicketXChange.TicketXChange.payment.repository;

import com.TicketXChange.TicketXChange.payment.model.PaymentToSellersByPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentToSellersByPlatformRepository extends JpaRepository<PaymentToSellersByPlatform, Long> {
}
