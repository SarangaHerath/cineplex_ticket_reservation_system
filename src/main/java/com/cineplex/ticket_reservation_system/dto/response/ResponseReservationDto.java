package com.cineplex.ticket_reservation_system.dto.response;

import com.cineplex.ticket_reservation_system.entity.Reservation;
import com.cineplex.ticket_reservation_system.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseReservationDto {
    private Long reservationId;
    private Status status;
    private LocalDate createdDate;
    private int noOfSeat;
    private ResponseMovieDto responseMovieDto;
    private ResponseShowTimeDto responseShowTimeDto;
    private ResponseUserDto responseUserDto;

    // Constructor to convert Reservation to ResponseReservationDto
    public static ResponseReservationDto fromReservation(Reservation reservation) {
        return ResponseReservationDto.builder()
                .reservationId(reservation.getReservationId())
                .status(reservation.getStatus())
                .createdDate(reservation.getCreatedDate())
                .noOfSeat(reservation.getNoOfSeat())
                .responseMovieDto(ResponseMovieDto.fromMovieEntity(reservation.getMovie()))
                .responseShowTimeDto(ResponseShowTimeDto.fromShowTimeEntity(reservation.getShowTime()))
                .responseUserDto(ResponseUserDto.fromUserEntity(reservation.getUser()))
                .build();
    }

}
