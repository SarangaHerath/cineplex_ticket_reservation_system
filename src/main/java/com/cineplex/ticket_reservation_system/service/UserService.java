package com.cineplex.ticket_reservation_system.service;


import com.cineplex.ticket_reservation_system.dto.request.RequestUserDto;
import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<CommonResponse> getAllUsers();

    ResponseEntity<CommonResponse> updateUser(RequestUserDto requestUserDto);

    ResponseEntity<CommonResponse> deleteUser(Long id);

    ResponseEntity<CommonResponse> findUserById(Long id);
}
