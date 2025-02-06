package com.TicketXChange.TicketXChange.ticket.model;

import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoughtTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "buyer_user_id", referencedColumnName = "id")
    private UserProfile buyer;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "ticketfeedback_id")
    private Long ticketFeedbackId;

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
