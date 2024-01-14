package com.cineplex.ticket_reservation_system.dto.request;

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
public class RequestReservationDto {

    private Long reservationId;
    private Status status;
    private LocalDate createdDate;
    private int noOfSeat;
    private Long movieId;
    private Long showTimeId;
    private Long userId;
}
