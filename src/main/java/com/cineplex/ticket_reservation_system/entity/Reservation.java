package com.cineplex.ticket_reservation_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reservation_id")
    private Long reservationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "no_of_seat",nullable = false)
    private int noOfSeat;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "movie_id", referencedColumnName = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "show_time_id", referencedColumnName = "show_time_id", nullable = false)
    private ShowTime showTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;
}
