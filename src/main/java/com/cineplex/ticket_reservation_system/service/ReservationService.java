package com.cineplex.ticket_reservation_system.service;

import com.cineplex.ticket_reservation_system.dto.request.RequestReservationDto;
import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface ReservationService {
    ResponseEntity<CommonResponse> saveReservation(RequestReservationDto requestReservationDto);

    ResponseEntity<CommonResponse> updateReservation(RequestReservationDto requestReservationDto);

    ResponseEntity<CommonResponse> getAllReservation();

    ResponseEntity<CommonResponse> getReservationById(Long id);

    ResponseEntity<CommonResponse> cancelReservation(Long id);

    ResponseEntity<CommonResponse> getReservationByUserId(Long id);

//    ResponseEntity<CommonResponse> changeStatus(RequestChangeStatusDto requestChangeStatusDto);
}
