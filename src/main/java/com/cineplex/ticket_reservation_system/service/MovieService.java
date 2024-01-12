package com.cineplex.ticket_reservation_system.service;

import com.cineplex.ticket_reservation_system.dto.request.RequestMovieDto;
import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface MovieService {
    ResponseEntity<CommonResponse> saveMovie(RequestMovieDto requestMovieSaveDto);

    ResponseEntity<CommonResponse> updateMovie(RequestMovieDto requestMovieDto);

    ResponseEntity<CommonResponse> getAllMovie();

    ResponseEntity<CommonResponse> getMovieById(Long id);

    ResponseEntity<CommonResponse> deleteMovie(Long id);
}
