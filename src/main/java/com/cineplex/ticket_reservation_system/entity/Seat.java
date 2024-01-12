package com.cineplex.ticket_reservation_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seat_id")
    private Long seatId;

    @Column(name = "no_of_seat", nullable = false)
    private int noOfSeats;

    @ManyToOne
    @JoinColumn(name = "show_time_id")
    private ShowTime showTime;
}
