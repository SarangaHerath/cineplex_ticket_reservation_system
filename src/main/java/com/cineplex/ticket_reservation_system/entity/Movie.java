package com.cineplex.ticket_reservation_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "movie_name", nullable = false)
    private String movieName;

    @Column(name = "movie_discription", nullable = false)
    private String movieDescription;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShowTime> showTimes;
}
