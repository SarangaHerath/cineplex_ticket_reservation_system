package com.cineplex.ticket_reservation_system.dto.response;

import com.cineplex.ticket_reservation_system.entity.Movie;
import com.cineplex.ticket_reservation_system.entity.ShowTime;
import com.cineplex.ticket_reservation_system.entity.Status;
import com.cineplex.ticket_reservation_system.entity.User;
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
    private Movie movie;
    private ShowTime showTime;
    private User user;
}
