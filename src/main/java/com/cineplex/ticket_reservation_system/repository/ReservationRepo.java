package com.cineplex.ticket_reservation_system.repository;

import com.cineplex.ticket_reservation_system.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation,Long> {
    boolean existsByMovie_MovieId(Long id);
}
