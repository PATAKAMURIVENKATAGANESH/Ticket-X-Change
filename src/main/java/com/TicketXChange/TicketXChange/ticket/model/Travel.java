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
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "travel_type")
    private String travelType; // BUS, TRAIN, FLIGHT

    @Column(name = "start_location")
    private String startLocation;

    @Column(name = "destination_location")
    private String destinationLocation;

    private LocalDateTime date;

    @Column(name = "number_of_tickets")
    private int numberOfTickets;

    @ElementCollection
    private List<String> seatNumbers;

    private LocalDateTime time;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
    }

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Ticket ticket;
}
