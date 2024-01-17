package com.cineplex.ticket_reservation_system.service.impl;

import com.cineplex.ticket_reservation_system.dto.request.RequestShowTimeDto;
import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import com.cineplex.ticket_reservation_system.dto.response.ResponseMovieDetailsDto;
import com.cineplex.ticket_reservation_system.dto.response.ResponseMovieDto;
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
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseEntity<CommonResponse> updateShowTime(RequestShowTimeDto requestShowTimeDto) {
        log.info("hit showTime save serviceImpl dto:{}", requestShowTimeDto);
        try {
            Optional<ShowTime> optionalShowTime = showTimeRepo.findById(requestShowTimeDto.getShowTimeId());
            if (optionalShowTime.isPresent()) {

                Movie movie = movieRepo.findMovieByMovieId(requestShowTimeDto.getMovieId());

                // update showTime table
                ShowTime showTime = optionalShowTime.get();
                showTime.setTime(requestShowTimeDto.getTime());
                showTime.setDate(requestShowTimeDto.getDate());
                showTime.setAvailableSeats(requestShowTimeDto.getAvailableSeats());
                showTime.setMovie(movie);

                showTimeRepo.save(showTime);

                // Convert the updated ShowTime entity to ResponseShowTimeDto
                ResponseShowTimeDto responseShowTimeDto = ResponseShowTimeDto.fromShowTimeEntity(showTime);

                return ResponseEntity.ok(CommonResponse.builder()
                        .responseCode(HttpStatus.OK)
                        .message("Showtime updated successfully")
                        .data(responseShowTimeDto)
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
        log.info("Entering getAllShowTime method");

        List<ResponseShowTimeDto> responseShowTimeDtoList = showTimeRepo.findAll().stream()
                .map(showTime -> ResponseShowTimeDto.builder()
                        .showTimeId(showTime.getShowTimeId())
                        .time(showTime.getTime())
                        .date(showTime.getDate())
                        .availableSeats(showTime.getAvailableSeats())
                        .responseMovieDto(showTime.getMovie().toResponseMovieDto())
                        .build())
                .toList();

        CommonResponse commonResponse = CommonResponse.builder()
                .message("Get all showTime success")
                .responseCode(HttpStatus.OK)
                .data(responseShowTimeDtoList)
                .build();

        log.info("Exiting getAllShowTime method");
        return ResponseEntity.ok(commonResponse);
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
                                .date(showTime.getDate())
                                .availableSeats(showTime.getAvailableSeats())
                                .responseMovieDto(showTime.getMovie().toResponseMovieDto())
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
    @Transactional
    public ResponseEntity<CommonResponse> deleteShowTime(Long id) {
        try {
            Optional<ShowTime> optionalShowTime = showTimeRepo.findById(id);
//            System.out.println(" show timeeeeeeeeeee: "+optionalShowTime.get());
            if (optionalShowTime.isPresent()) {

                ShowTime showTime = optionalShowTime.get();
//                System.out.println("awaa iffff");
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

    @Override
    public ResponseEntity<CommonResponse> getMovieDetails(Long id) {
        try {
            if (movieRepo.existsById(id)) {
                Movie movie = movieRepo.findMovieByMovieId(id);
                ResponseMovieDto responseMovieDto = ResponseMovieDto.builder()
                        .movieName(movie.getMovieName())
                        .movieDescription(movie.getMovieDescription())
                        .movieId(movie.getMovieId())
                        .build();

                List<ShowTime> showTimeList = showTimeRepo.findShowTimeByMovie_MovieId(id);
                List<ResponseShowTimeDto> responseShowTimeDtoList = showTimeList.stream()
                        .map(showTime -> ResponseShowTimeDto.builder()
                                .showTimeId(showTime.getShowTimeId())
                                .time(showTime.getTime())
                                .date(showTime.getDate())
                                .availableSeats(showTime.getAvailableSeats())
                                .responseMovieDto(showTime.getMovie().toResponseMovieDto())
                                .build())
                        .toList();
                return ResponseEntity.ok(CommonResponse.builder()
                        .responseCode(HttpStatus.OK)
                        .message("get movie and show time details success")
                        .data(ResponseMovieDetailsDto.builder()
                                .responseMovieDto(responseMovieDto)
                                .responseShowTimeDtoList(responseShowTimeDtoList)
                                .build())
                        .build());

            } else {
                throw new ResourceNotFoundException("Can not find that movie");
            }
        } catch (Exception e) {
            throw new InternalServerException("Error occur get movie and show time details");
        }

    }

    @Override
    public ResponseEntity<CommonResponse> getAvailableSeatById(Long id) {
        try {
            ShowTime showTime = showTimeRepo.findById(id).get();
            int availableSeats = showTime.getAvailableSeats();
            return ResponseEntity.ok(CommonResponse.builder()
                    .message("Success get available seats")
                    .responseCode(HttpStatus.OK)
                    .data(availableSeats)
                    .build());
        } catch (Exception e) {
            throw new InternalServerException("error during get available seat");
        }

    }
}
