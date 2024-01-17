package com.cineplex.ticket_reservation_system.repository;

import com.cineplex.ticket_reservation_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String userName);


    boolean findUserByUsername(String username);
}
