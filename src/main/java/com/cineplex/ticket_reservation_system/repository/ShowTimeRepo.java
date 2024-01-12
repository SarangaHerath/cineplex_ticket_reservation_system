package com.cineplex.ticket_reservation_system.repository;

import com.cineplex.ticket_reservation_system.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowTimeRepo extends JpaRepository<ShowTime, Long> {

}
