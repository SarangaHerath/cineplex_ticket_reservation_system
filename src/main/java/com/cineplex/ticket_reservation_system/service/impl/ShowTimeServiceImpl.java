package com.cineplex.ticket_reservation_system.service.impl;

import com.cineplex.ticket_reservation_system.dto.request.RequestShowTimeDto;
import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import com.cineplex.ticket_reservation_system.dto.response.ResponseShowTimeDto;
import com.cineplex.ticket_reservation_system.entity.Movie;
import com.cineplex.ticket_reservation_system.entity.ShowTime;
import com.cineplex.ticket_reservation_system.exceptions.InternalServerException;
import com.cineplex.ticket_reservation_system.exceptions.ResourceNotFoundException;
import com.cineplex.ticket_reservation_system.repository.MovieRepo;
import com.cineplex.ticket_reservation_system.repository.ShowTimeRepo;
import com.cineplex.ticket_reservation_system.service.ShowTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ShowTimeServiceImpl implements ShowTimeService {

    private final ShowTimeRepo showTimeRepo;
    private final MovieRepo movieRepo;

    public ShowTimeServiceImpl(ShowTimeRepo showTimeRepo, MovieRepo movieRepo) {
        this.showTimeRepo = showTimeRepo;
        this.movieRepo = movieRepo;
    }

    @Override
    public ResponseEntity<CommonResponse> saveShowTime(RequestShowTimeDto requestShowTimeDto) {
        log.info("hit showTime save serviceImpl dto:{}", requestShowTimeDto);
        try {
            if (showTimeRepo.existsByTime(requestShowTimeDto.getTime())) {
                throw new ResourceNotFoundException("This showTime already saved");
            } else {
                Movie movie = movieRepo.findMovieByMovieId(requestShowTimeDto.getMovieId());
                ShowTime showTime = ShowTime.builder()
                        .time(requestShowTimeDto.getTime())
                        .availableSeats(requestShowTimeDto.getAvailableSeats())
                        .movie(movie)
                        .build();
                showTimeRepo.save(showTime);

                return ResponseEntity.ok(CommonResponse.builder()
                        .message("Show time saved successfully")
                        .responseCode(HttpStatus.CREATED)
                        .data(showTime)
                        .build());
            }
        } catch (Exception e) {
            log.error("Error saving showTime: {}", e.getMessage());
            e.printStackTrace();
            throw new InternalServerException("Error saving showTime");
        }

    }

    @Override
    public ResponseEntity<CommonResponse> updateShowTime(RequestShowTimeDto requestShowTimeDto) {
        log.info("hit showTime save serviceImpl dto:{}", requestShowTimeDto);
        try {
            Optional<ShowTime> optionalShowTime = showTimeRepo.findById(requestShowTimeDto.getShowTimeId());
            if (optionalShowTime.isPresent()) {

                Movie movie = movieRepo.findMovieByMovieId(requestShowTimeDto.getMovieId());

                // update showTime table
                ShowTime showTime = optionalShowTime.get();
                showTime.setTime(requestShowTimeDto.getTime());
                showTime.setAvailableSeats(requestShowTimeDto.getAvailableSeats());
                showTime.setMovie(movie);

                showTimeRepo.save(showTime);

                return ResponseEntity.ok(CommonResponse.builder()
                        .responseCode(HttpStatus.OK)
                        .message("Showtime updated successfully")
                        .data(showTime)
                        .build());

            } else {
                throw new ResourceNotFoundException("Can't find this showTime");
            }
        } catch (Exception e) {
            log.error("Error updating showTime: {}", e.getMessage());
            throw new InternalServerException("Error updating showTime");
        }

    }

    @Override
    public ResponseEntity<CommonResponse> getAllShowTime() {
        log.info("hit get all showTime service Impl");
        try {
            List<ResponseShowTimeDto> responseShowTimeDtoList = showTimeRepo.findAll().stream()
                    .map(showTime -> ResponseShowTimeDto.builder()
                            .showTimeId(showTime.getShowTimeId())
                            .time(showTime.getTime())
                            .availableSeats(showTime.getAvailableSeats())
                            .movie(showTime.getMovie())
                            .build())
                    .toList();
            return ResponseEntity.ok(CommonResponse.builder()
                    .message("Get all showTime success")
                    .responseCode(HttpStatus.OK)
                    .data(responseShowTimeDtoList)
                    .build());
        } catch (Exception e) {
            log.error("Error get all showTime", e.getMessage());
            throw new InternalServerException("Error get all showTime");
        }

    }

    @Override
    public ResponseEntity<CommonResponse> getShowTimeById(Long id) {
        log.info("hit get showTime by id service Impl");
        try {
            if (showTimeRepo.existsById(id)) {

                ShowTime showTime = showTimeRepo.findShowTimeByShowTimeId(id);
                return ResponseEntity.ok(CommonResponse.builder()
                        .data(ResponseShowTimeDto.builder()
                                .showTimeId(showTime.getShowTimeId())
                                .time(showTime.getTime())
                                .availableSeats(showTime.getAvailableSeats())
                                .movie(showTime.getMovie())
                                .build())
                        .responseCode(HttpStatus.OK)
                        .message("Get showTime by id success")
                        .build());
            } else {
                throw new ResourceNotFoundException("Can't find this showTime");
            }
        } catch (Exception e) {
            log.error("Error get showTime by id: {}", id, e.getMessage());
            throw new InternalServerException("Error get showTime details");
        }
    }

    @Override
    public ResponseEntity<CommonResponse> deleteShowTime(Long id) {
        try {
            Optional<ShowTime> optionalShowTime = showTimeRepo.findById(id);
            if (optionalShowTime.isPresent()) {

                ShowTime showTime = optionalShowTime.get();

                // delete showTime table data
                showTimeRepo.delete(showTime);

                return ResponseEntity.ok(CommonResponse.builder()
                        .message("Delete success")
                        .responseCode(HttpStatus.NO_CONTENT)
                        .build());
            } else {
                throw new ResourceNotFoundException("Movie not found");
            }
        } catch (Exception e) {
            log.error("Error delete showTime id: {}", id, e.getMessage());
            throw new InternalServerException("Error delete showTime");
        }
    }
}
