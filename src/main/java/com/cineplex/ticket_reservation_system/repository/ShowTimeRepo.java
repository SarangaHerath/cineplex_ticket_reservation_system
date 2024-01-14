package com.cineplex.ticket_reservation_system.repository;

import com.cineplex.ticket_reservation_system.entity.Movie;
import com.cineplex.ticket_reservation_system.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ShowTimeRepo extends JpaRepository<ShowTime, Long> {

    boolean existsByTime(LocalTime time);

    ShowTime findShowTimeByShowTimeId(Long showTimeId);

    List<ShowTime> findShowTimeByMovie_MovieId(Long id);
}
