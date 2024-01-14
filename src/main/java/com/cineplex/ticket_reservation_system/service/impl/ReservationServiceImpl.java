package com.cineplex.ticket_reservation_system.service.impl;

import com.cineplex.ticket_reservation_system.dto.request.RequestReservationDto;
import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import com.cineplex.ticket_reservation_system.dto.response.ResponseReservationDto;
import com.cineplex.ticket_reservation_system.entity.*;
import com.cineplex.ticket_reservation_system.exceptions.InternalServerException;
import com.cineplex.ticket_reservation_system.exceptions.ResourceNotFoundException;
import com.cineplex.ticket_reservation_system.repository.MovieRepo;
import com.cineplex.ticket_reservation_system.repository.ReservationRepo;
import com.cineplex.ticket_reservation_system.repository.ShowTimeRepo;
import com.cineplex.ticket_reservation_system.repository.UserRepo;
import com.cineplex.ticket_reservation_system.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepo reservationRepo;
    private final MovieRepo movieRepo;
    private final ShowTimeRepo showTimeRepo;
    private final UserRepo userRepo;

    public ReservationServiceImpl(ReservationRepo reservationRepo, MovieRepo movieRepo, ShowTimeRepo showTimeRepo, UserRepo userRepo) {
        this.reservationRepo = reservationRepo;
        this.movieRepo = movieRepo;
        this.showTimeRepo = showTimeRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> saveReservation(RequestReservationDto requestReservationDto) {
        try {
            log.info("hit reservation save serviceImpl dto:{}", requestReservationDto);
            Movie movie = movieRepo.findMovieByMovieId(requestReservationDto.getMovieId());
            ShowTime showTime = showTimeRepo.findShowTimeByShowTimeId(requestReservationDto.getShowTimeId());
            User user = userRepo.findById(requestReservationDto.getUserId()).get();

            Reservation reservation = Reservation.builder()
                    .createdDate(LocalDate.now())
                    .noOfSeat(requestReservationDto.getNoOfSeat())
                    .status(Status.RESERVE)
                    .movie(movie)
                    .showTime(showTime)
                    .user(user)
                    .build();

            reservationRepo.save(reservation);

            // Update availableSeat in ShowTime based on noOfSeatBook in Reservation
            Long showTimeId = requestReservationDto.getShowTimeId();
            int noOfSeatBook = requestReservationDto.getNoOfSeat();
            ShowTime showTime1 = showTimeRepo.findById(showTimeId)
                    .orElseThrow(() -> new ResourceNotFoundException("ShowTime not found with id: " + showTimeId));

            // Ensure available seats are not negative
            int updatedAvailableSeats = Math.max(0, showTime1.getAvailableSeats() - noOfSeatBook);
            showTime1.setAvailableSeats(updatedAvailableSeats);

            // Save the updated ShowTime entity
            showTimeRepo.save(showTime1);

            return ResponseEntity.ok(CommonResponse.builder()

                    .message("Reservation save successful")
                    .responseCode(HttpStatus.CREATED)
                    .build());

        } catch (Exception e) {
            log.error("Error saving reservation : {}", e.getMessage());
            throw new InternalServerException("Error occur during save reservation");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> updateReservation(RequestReservationDto requestReservationDto) {
        log.info("hit updateReservation serviceImpl: {}",requestReservationDto);
        try {
            Long reservationId = requestReservationDto.getReservationId();
            Reservation existingReservation = reservationRepo.findById(reservationId)
                    .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

            // Check if noOfSeat has changed
            int oldNoOfSeat = existingReservation.getNoOfSeat();
            int newNoOfSeat = requestReservationDto.getNoOfSeat();

            if (oldNoOfSeat != newNoOfSeat) {
                // Update availableSeat in ShowTime based on the change in noOfSeat
                Long showTimeId = existingReservation.getShowTime().getShowTimeId();
                ShowTime showTime = showTimeRepo.findById(showTimeId)
                        .orElseThrow(() -> new ResourceNotFoundException("ShowTime not found with id: " + showTimeId));

                // Calculate the change in available seats
                int seatChange = newNoOfSeat - oldNoOfSeat;

                // Ensure available seats are not negative
                int updatedAvailableSeats = Math.max(0, showTime.getAvailableSeats() - seatChange);
                showTime.setAvailableSeats(updatedAvailableSeats);

                // Save the updated ShowTime entity
                showTimeRepo.save(showTime);

            }
            // Update other fields in the Reservation entity based on requestReservationDto
            existingReservation.setNoOfSeat(newNoOfSeat);
            existingReservation.setCreatedDate(LocalDate.now());


            // Save the updated Reservation entity
            Reservation savedReservation = reservationRepo.save(existingReservation);

            return ResponseEntity.ok(CommonResponse.builder()
                    .data(savedReservation)
                    .responseCode(HttpStatus.OK)
                    .message("Update successful")
                    .build());

        } catch (Exception e) {
            log.error("Error updating reservation : {}", e.getMessage());
            throw new InternalServerException("Error occur during update reservation");
        }
    }

    @Override
    public ResponseEntity<CommonResponse> getAllReservation() {
        log.info("hit get all reservation service Impl");
        try {
            List<ResponseReservationDto> responseReservationDtoList = reservationRepo.findAll().stream()
                    .map(reservation -> ResponseReservationDto.builder()
                            .reservationId(reservation.getReservationId())
                            .createdDate(reservation.getCreatedDate())
                            .noOfSeat(reservation.getNoOfSeat())
                            .status(reservation.getStatus())
                            .movie(reservation.getMovie())
                            .showTime(reservation.getShowTime())
                            .user(reservation.getUser())
                            .build())
                    .toList();
            return ResponseEntity.ok(CommonResponse.builder()
                            .message("Get all reservation successful")
                            .responseCode(HttpStatus.OK)
                            .data(responseReservationDtoList)
                    .build());
        }
        catch (Exception e){
            log.error("Error get all reservation : {}", e.getMessage());
            throw new InternalServerException("Error occur during get all reservation");
        }
    }

    @Override
    public ResponseEntity<CommonResponse> getReservationById(Long id) {
        try {
            if (reservationRepo.existsById(id)) {
                Optional<Reservation> reservationOptional = reservationRepo.findById(id);
                if (reservationOptional.isPresent()) {
                    Reservation reservation = reservationOptional.get();

                    ResponseReservationDto responseReservationDto = ResponseReservationDto.builder()
                            .reservationId(reservation.getReservationId())
                            .noOfSeat(reservation.getNoOfSeat())
                            .createdDate(reservation.getCreatedDate())
                            .status(reservation.getStatus())
                            .movie(reservation.getMovie())
                            .showTime(reservation.getShowTime())
                            .user(reservation.getUser())
                            .build();
                    return ResponseEntity.ok(CommonResponse.builder()
                            .data(responseReservationDto)
                            .responseCode(HttpStatus.OK)
                            .message("Reservation get by id success")
                            .build());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(CommonResponse.builder()
                                    .message("Can not find reservation")
                                    .responseCode(HttpStatus.NOT_FOUND)
                                    .build());
                }
            }
        } catch (Exception e) {
            throw new InternalServerException("Error occur during get by id reservation");
        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonResponse.builder()
                        .responseCode(HttpStatus.NOT_FOUND)
                        .message("Reservation not found")
                        .build());
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> cancelReservation(Long id) {
        try {
            if(reservationRepo.existsById(id)){
                // delete
                Reservation reservation = reservationRepo.findById(id).get();
                reservation.setStatus(Status.CANCEL);
                reservationRepo.save(reservation);

                // update available seats
                int seatQty = reservation.getNoOfSeat();
                ShowTime showTime = showTimeRepo.findShowTimeByShowTimeId(reservation.getShowTime().getShowTimeId());
                showTime.setAvailableSeats(showTime.getAvailableSeats()+seatQty);
                showTimeRepo.save(showTime);

                return ResponseEntity.ok(CommonResponse.builder()
                                .message("Delete success")
                                .responseCode(HttpStatus.NO_CONTENT)
                        .build());
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CommonResponse.builder()
                                .message("Can not find reservation with this id")
                                .responseCode(HttpStatus.NOT_FOUND)
                                .build());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new InternalServerException("Error occur during delete reservation");

        }
    }

}

