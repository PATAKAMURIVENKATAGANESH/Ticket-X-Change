package com.TicketXChange.TicketXChange.ticket.model;

import com.TicketXChange.TicketXChange.ticket.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_mail", nullable = false)
    private String ownerMail;

    @Column(name = "original_owner_mail", nullable = false)
    private String originalOwnerMail;

//    @Column(name = "email", nullable = false)
//    private String email;

    private String mailNumber;

    @Column(name = "ticket_url",  columnDefinition = "VARCHAR(1000)")
    private String ticketUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VerificationStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "id", nullable = false)
    private Ticket ticket;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
