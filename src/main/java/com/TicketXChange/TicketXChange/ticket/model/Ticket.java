package com.TicketXChange.TicketXChange.ticket.model;

import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.ticket.enums.AvailabilityStatus;
import com.TicketXChange.TicketXChange.ticket.enums.Category;
import com.TicketXChange.TicketXChange.ticket.enums.VerificationTag;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketId;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private VerificationTag tag;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    private boolean mailForwarded;


    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserProfile user;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
