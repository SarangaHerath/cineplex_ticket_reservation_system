package com.cineplex.ticket_reservation_system.repository;

import com.cineplex.ticket_reservation_system.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Long> {
    boolean existsByMovieName(String movieName);

    Movie findMovieByMovieId(Long id);
}
