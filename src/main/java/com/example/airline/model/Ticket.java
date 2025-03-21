package com.example.airline.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passengerName;
    private String seatNumber;
    private Double price;
    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;
}
