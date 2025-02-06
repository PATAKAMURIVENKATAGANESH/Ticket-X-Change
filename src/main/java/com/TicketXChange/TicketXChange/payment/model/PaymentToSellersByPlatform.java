package com.TicketXChange.TicketXChange.payment.model;

import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.payment.enums.PaidByPlatformStatus;
import com.TicketXChange.TicketXChange.ticket.model.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentToSellersByPlatform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id")
    private Long paymentId;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "seller_user_id", referencedColumnName = "id")
    private UserProfile user;

    @Enumerated(EnumType.STRING)
    @Column(name = "paidbyplatform_status")
    private PaidByPlatformStatus paidByPlatformStatus;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
