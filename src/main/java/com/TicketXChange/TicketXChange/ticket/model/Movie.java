package com.TicketXChange.TicketXChange.ticket.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String movie;
    private String theatre;
    private LocalDateTime fromDate;

    @Column(name = "number_of_tickets")
    private int numberOfTickets;

    @ElementCollection
    private List<String> seats;

    private LocalDateTime toDate;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
    }

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

}
