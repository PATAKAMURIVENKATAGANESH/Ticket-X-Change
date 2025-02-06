package com.TicketXChange.TicketXChange.payment.model;

import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.payment.enums.PaymentStatus;
import com.TicketXChange.TicketXChange.ticket.model.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "transaction_id")
    private String transactionId;

    private String orderId;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "buyer_user_id", referencedColumnName = "id")
    private UserProfile buyer;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "seller_user_id", referencedColumnName = "id")
    private UserProfile seller;

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
