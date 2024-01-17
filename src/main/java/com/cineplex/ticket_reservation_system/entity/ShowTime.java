package com.cineplex.ticket_reservation_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "show_time")
public class ShowTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "show_time_id")
    private Long showTimeId;

    @Column(name = "time", columnDefinition = "TIME")
    private LocalTime time;

    @Column(name = "available_seats")
    private int availableSeats;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "movie_id", nullable = false)
    private Movie movie;
}
