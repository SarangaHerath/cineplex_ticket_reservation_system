package com.cineplex.ticket_reservation_system.service;

import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface ShowTimeService {

//    ResponseEntity<CommonResponse> updateShowTime(RequestShowTimeDto requestShowTimeDto);

    ResponseEntity<CommonResponse> getAllShowTime();

    ResponseEntity<CommonResponse> getShowTimeById(Long id);

//    ResponseEntity<CommonResponse> deleteShowTime(Long id);

    ResponseEntity<CommonResponse> getMovieDetails(Long id);
}
