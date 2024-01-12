package com.cineplex.ticket_reservation_system.service.impl;

import com.cineplex.ticket_reservation_system.dto.request.RequestMovieDto;
import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import com.cineplex.ticket_reservation_system.dto.response.ResponseMovieDto;
import com.cineplex.ticket_reservation_system.entity.Movie;
import com.cineplex.ticket_reservation_system.exceptions.InternalServerException;
import com.cineplex.ticket_reservation_system.exceptions.ResourceNotFoundException;
import com.cineplex.ticket_reservation_system.repository.MovieRepo;
import com.cineplex.ticket_reservation_system.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final MovieRepo movieRepo;

    public MovieServiceImpl(MovieRepo movieRepo) {
        this.movieRepo = movieRepo;
    }

    @Override
    public ResponseEntity<CommonResponse> saveMovie(RequestMovieDto requestMovieDto) {
        log.info("hit movie save serviceImpl dto:{}", requestMovieDto);
        try {
            if (movieRepo.existsByMovieName(requestMovieDto.getMovieName())) {
                throw new ResourceNotFoundException("This movie already saved");
            } else {

                Movie movie = Movie.builder()
                        .movieName(requestMovieDto.getMovieName())
                        .movieDescription(requestMovieDto.getMovieDescription())
                        .build();
                movieRepo.save(movie);

                return ResponseEntity.ok(CommonResponse.builder()
                        .message("Movie, showtime, and seats saved successfully")
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
    public ResponseEntity<CommonResponse> updateMovie(RequestMovieDto requestMovieDto) {
        log.info("hit movie save serviceImpl dto:{}", requestMovieDto);
        try {
            Optional<Movie> optionalMovie = movieRepo.findById(requestMovieDto.getId());
            if (optionalMovie.isPresent()) {

                // update movie table
                Movie movie = optionalMovie.get();
                movie.setMovieName(requestMovieDto.getMovieName());
                movie.setMovieDescription(requestMovieDto.getMovieDescription());

                movieRepo.save(movie);

                return ResponseEntity.ok(CommonResponse.builder()
                        .responseCode(HttpStatus.OK)
                        .message("Movie, showtime, and seats updated successfully")
                        .data(movie)
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
            if (movieRepo.existsById(id)) {
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
            } else {
                throw new ResourceNotFoundException("Can't find this movie");
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
                // delete movie table data
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
            throw new InternalServerException("Error delete movie");
        }
    }


}
