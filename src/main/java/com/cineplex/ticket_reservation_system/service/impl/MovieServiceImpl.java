package com.cineplex.ticket_reservation_system.service.impl;

import com.cineplex.ticket_reservation_system.dto.request.RequestMovieDto;
import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import com.cineplex.ticket_reservation_system.dto.response.ResponseMovieDto;
import com.cineplex.ticket_reservation_system.entity.Movie;
import com.cineplex.ticket_reservation_system.entity.ShowTime;
import com.cineplex.ticket_reservation_system.exceptions.InternalServerException;
import com.cineplex.ticket_reservation_system.exceptions.ResourceNotFoundException;
import com.cineplex.ticket_reservation_system.repository.MovieRepo;
import com.cineplex.ticket_reservation_system.repository.ReservationRepo;
import com.cineplex.ticket_reservation_system.repository.ShowTimeRepo;
import com.cineplex.ticket_reservation_system.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final MovieRepo movieRepo;
    private final ShowTimeRepo showTimeRepo;
    private final ReservationRepo reservationRepo;

    public MovieServiceImpl(MovieRepo movieRepo, ShowTimeRepo showTimeRepo, ReservationRepo reservationRepo) {
        this.movieRepo = movieRepo;
        this.showTimeRepo = showTimeRepo;
        this.reservationRepo = reservationRepo;
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> saveMovie(RequestMovieDto requestMovieDto) {
        log.info("hit movie save serviceImpl dto:{}", requestMovieDto);
        try {
            if (movieRepo.existsByMovieName(requestMovieDto.getMovieName())) {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                        .body(CommonResponse.builder()
                                .message("This movie already saved")
                                .responseCode(HttpStatus.ALREADY_REPORTED)
                                .build());
            } else {

                // movie
                Movie movie = Movie.builder()
                        .movieName(requestMovieDto.getMovieName())
                        .movieDescription(requestMovieDto.getMovieDescription())
                        .build();
                Movie saveMovie = movieRepo.save(movie);

                // show times

                requestMovieDto.getRequestShowTimeDtoList().stream()
                        .map(requestShowTimeDto -> {
                            ShowTime showTime = ShowTime.builder()
                                    .showTimeId(requestShowTimeDto.getShowTimeId())
                                    .time(requestShowTimeDto.getTime())
                                    .date(requestShowTimeDto.getDate())
                                    .availableSeats(requestShowTimeDto.getAvailableSeats())
                                    .movie(saveMovie)
                                    .build();
                            return showTimeRepo.save(showTime);
                        })
                        .toList();

                return ResponseEntity.ok(CommonResponse.builder()
                        .message("Movie, showtime saved successfully")
                        .responseCode(HttpStatus.CREATED)
                        .data(movie)
                        .build());
            }
        } catch (Exception e) {
            log.error("Error saving movie: {}", e.getMessage());
            e.printStackTrace();
            throw new InternalServerException("Error saving movie");
        }

    }


    @Override
    @Transactional
    public ResponseEntity<CommonResponse> updateMovie(RequestMovieDto requestMovieDto) {
        log.info("hit movie save serviceImpl dto:{}", requestMovieDto);
        try {
            Optional<Movie> optionalMovie = movieRepo.findById(requestMovieDto.getMovieId());
            if (optionalMovie.isPresent()) {

                // update movie table
                Movie movie = optionalMovie.get();
                movie.setMovieName(requestMovieDto.getMovieName());
                movie.setMovieDescription(requestMovieDto.getMovieDescription());

                Movie updateMovie = movieRepo.save(movie);

                // update show time
                requestMovieDto.getRequestShowTimeDtoList().stream()
                        .map(requestShowTimeDto -> {
                            ShowTime showTime = showTimeRepo.findById(requestShowTimeDto.getShowTimeId()).get();
                            showTime.setTime(requestShowTimeDto.getTime());
                            showTime.setDate(requestShowTimeDto.getDate());
                            showTime.setAvailableSeats(requestShowTimeDto.getAvailableSeats());
                            showTime.setMovie(updateMovie);
                            return showTimeRepo.save(showTime);
                        })
                        .collect(Collectors.toList());


                return ResponseEntity.ok(CommonResponse.builder()
                        .responseCode(HttpStatus.OK)
                        .message("Movie, showtime updated successfully")
                        .build());

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CommonResponse.builder()
                                .message("Can't find this movie")
                                .responseCode(HttpStatus.NOT_FOUND)
                                .build());
            }
        } catch (Exception e) {
            log.error("Error updating movie: {}", e.getMessage());
            throw new InternalServerException("Error updating movie");
        }

    }

    @Override
    public ResponseEntity<CommonResponse> getAllMovie() {
        log.info("hit get all movie service Impl");
        try {
            List<ResponseMovieDto> responseMovieDtoList = movieRepo.findAll().stream()
                    .map(movie -> ResponseMovieDto.builder()
                            .movieId(movie.getMovieId())
                            .movieDescription(movie.getMovieName())
                            .movieName(movie.getMovieName())
                            .build())
                    .toList();
            return ResponseEntity.ok(CommonResponse.builder()
                    .message("Get all movie success")
                    .responseCode(HttpStatus.OK)
                    .data(responseMovieDtoList)
                    .build());
        } catch (Exception e) {
            log.error("Error get all movie", e.getMessage());
            throw new InternalServerException("Error get all movie");
        }

    }

    @Override
    public ResponseEntity<CommonResponse> getMovieById(Long id) {
        log.info("hit get movie by id service Impl");
        try {
            if (movieRepo.existsById(id)) {
                Movie movie = movieRepo.findMovieByMovieId(id);
                return ResponseEntity.ok(CommonResponse.builder()
                        .data(RequestMovieDto.builder()
                                .movieId(movie.getMovieId())
                                .movieName(movie.getMovieName())
                                .movieDescription(movie.getMovieDescription())
                                .build())
                        .responseCode(HttpStatus.OK)
                        .message("Get movie by id success")
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CommonResponse.builder()
                                .message("Can't find this movie")
                                .responseCode(HttpStatus.NOT_FOUND)
                                .build());
            }
        } catch (Exception e) {
            log.error("Error get movie by id: {}", id, e.getMessage());
            throw new InternalServerException("Error get movie details");
        }
    }

    @Override
    public ResponseEntity<CommonResponse> deleteMovie(Long id) {
        try {
            Optional<Movie> optionalMovie = movieRepo.findById(id);
            if (optionalMovie.isPresent()) {

                Movie movie = optionalMovie.get();

                // Check if there are reservations associated with the movie
                boolean hasReservations = reservationRepo.existsByMovie_MovieId(id);
                if (hasReservations) {
                    return ResponseEntity.badRequest().body(CommonResponse.builder()
                            .responseCode(HttpStatus.BAD_REQUEST)
                            .message("Cannot delete the movie because there are reservations.")
                            .build());
                }

                // Fetch associated show times
                List<ShowTime> showTimeList = showTimeRepo.findShowTimeByMovie_MovieId(id);

                // Delete show times associated with the movie
                showTimeRepo.deleteAll(showTimeList);

                // Delete the movie
                movieRepo.delete(movie);

                return ResponseEntity.ok(CommonResponse.builder()
                        .message("Delete success")
                        .responseCode(HttpStatus.NO_CONTENT)
                        .build());
            } else {
                throw new ResourceNotFoundException("Movie not found");
            }
        } catch (Exception e) {
            log.error("Error delete movie id: {}", id, e.getMessage());
            e.printStackTrace();
            throw new InternalServerException("Error delete movie");
        }
    }


}
