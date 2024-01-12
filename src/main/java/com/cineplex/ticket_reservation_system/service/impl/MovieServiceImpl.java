package com.cineplex.ticket_reservation_system.service.impl;

import com.cineplex.ticket_reservation_system.dto.request.RequestMovieDto;
import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import com.cineplex.ticket_reservation_system.dto.response.ResponseMovieDto;
import com.cineplex.ticket_reservation_system.entity.Movie;
import com.cineplex.ticket_reservation_system.entity.Seat;
import com.cineplex.ticket_reservation_system.entity.ShowTime;
import com.cineplex.ticket_reservation_system.exceptions.InternalServerException;
import com.cineplex.ticket_reservation_system.exceptions.ResourceNotFoundException;
import com.cineplex.ticket_reservation_system.repository.MovieRepo;
import com.cineplex.ticket_reservation_system.repository.SeatRepo;
import com.cineplex.ticket_reservation_system.repository.ShowTimeRepo;
import com.cineplex.ticket_reservation_system.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final MovieRepo movieRepo;
    private final ShowTimeRepo showTimeRepo;
    private final SeatRepo seatRepo;

    public MovieServiceImpl(MovieRepo movieRepo, ShowTimeRepo showTimeRepo, SeatRepo seatRepo) {
        this.movieRepo = movieRepo;
        this.showTimeRepo = showTimeRepo;
        this.seatRepo = seatRepo;
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> saveMovie(RequestMovieDto requestMovieDto) {
        log.info("hit movie save serviceImpl dto:{}", requestMovieDto);
        try {

            if (movieRepo.existsByMovieName(requestMovieDto.getMovieName())) {
                throw new ResourceNotFoundException("This movie already saved");
            } else {
                // movie save
                Movie movie = Movie.builder()
                        .movieName(requestMovieDto.getMovieName())
                        .movieDescription(requestMovieDto.getMovieDescription())
                        .build();
                Movie saveMovie = movieRepo.save(movie);

                // show times
                List<ShowTime> showTimeList = requestMovieDto.getRequestShowTimeDtoList().stream()
                        .map(requestShowTimeDto -> ShowTime.builder()
                                .showTimeId(requestShowTimeDto.getShowTimeId())
                                .time(requestShowTimeDto.getTime())
                                .movie(saveMovie)
                                .build())
                        .toList();
                showTimeRepo.saveAll(showTimeList);

                // seats

                List<Seat> seatList = requestMovieDto.getRequestSeatDtoList().stream()
                        .map(requestSeatDto -> Seat.builder()
                                .seatId(requestSeatDto.getSeatId())
                                .noOfSeats(requestSeatDto.getNoOfSeats())
                                .showTime(showTimeList.stream()
                                        .filter(showTime -> showTime.getShowTimeId().equals(requestSeatDto.getShowTimeId()))
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException("ShowTime not found for Seat")))
                                .build())
                        .toList();
                seatRepo.saveAll(seatList);

                return ResponseEntity.ok(CommonResponse.builder()
                        .message("Movie, showtime, and seats saved successfully")
                        .responseCode(HttpStatus.CREATED)
                        .build());
            }
        } catch (Exception e) {
            log.error("Error saving movie: {}", e.getMessage());
            throw new InternalServerException("Error saving movie");
        }

    }

    @Override
    public ResponseEntity<CommonResponse> updateMovie(RequestMovieDto requestMovieDto) {
        log.info("hit movie save serviceImpl dto:{}", requestMovieDto);
        try {
            Optional<Movie> optionalMovie = movieRepo.findById(requestMovieDto.getId());
            if (optionalMovie.isPresent()) {

                // update movie table
                Movie movie = optionalMovie.get();
                movie.setMovieName(requestMovieDto.getMovieName());
                movie.setMovieDescription(requestMovieDto.getMovieDescription());

                Movie updateMovie = movieRepo.save(movie);

                // update show times table
                List<ShowTime> showTimeList = requestMovieDto.getRequestShowTimeDtoList().stream()
                        .map(
                                requestShowTimeDto -> {
                                    ShowTime showTime = showTimeRepo.findById(requestShowTimeDto.getShowTimeId())
                                            .orElseThrow(() -> new ResourceNotFoundException("ShowTime not found with id: " + requestShowTimeDto.getShowTimeId()));
                                    showTime.setTime(requestShowTimeDto.getTime());
                                    showTime.setMovie(updateMovie);
                                    return showTimeRepo.save(showTime);
                                }
                        )
                        .toList();

                // update seats table

                requestMovieDto.getRequestSeatDtoList().stream()
                        .map(requestSeatDto -> {
                            Seat seat = seatRepo.findById(requestSeatDto.getSeatId())
                                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + requestSeatDto.getSeatId()));
                            seat.setNoOfSeats(requestSeatDto.getNoOfSeats());
                            ShowTime showTime = showTimeList.stream()
                                    .filter(st -> st.getShowTimeId().equals(requestSeatDto.getShowTimeId()))
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("ShowTime not found for Seat"));
                            seat.setShowTime(showTime);
                            return seatRepo.save(seat);
                        })
                        .toList();
                return ResponseEntity.ok(CommonResponse.builder()
                        .responseCode(HttpStatus.OK)
                        .message("Movie, showtime, and seats updated successfully")
                        .build());

            } else {
                throw new ResourceNotFoundException("Can't find this movie");
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
            if(movieRepo.existsById(id)){
                Movie movie = movieRepo.findMovieByMovieId(id);
                return ResponseEntity.ok(CommonResponse.builder()
                                .data(RequestMovieDto.builder()
                                        .id(movie.getMovieId())
                                        .movieName(movie.getMovieName())
                                        .movieDescription(movie.getMovieDescription())
                                        .build())
                                .responseCode(HttpStatus.OK)
                                .message("Get movie by id success")
                        .build());
            }
            else {
                throw new ResourceNotFoundException("Can't find this movie");
            }
        }catch (Exception e){
            log.error("Error get movie by id: {}",id, e.getMessage());
            throw new InternalServerException("Error get movie details");
        }
    }


}
