package com.TicketXChange.TicketXChange.auth.model;

import com.TicketXChange.TicketXChange.auth.enums.ReasonEnum;
import com.TicketXChange.TicketXChange.auth.enums.ReasonEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reason")
public class Reason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "enum_reason")
    @Enumerated
    private ReasonEnum enumReason;

    @Column(name = "description")
    private String description;
}
