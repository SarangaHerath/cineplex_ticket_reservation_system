package com.cineplex.ticket_reservation_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RequestShowTimeDto {

    private Long showTimeId;
    private Long movieId;
    private int availableSeats;
    private LocalDate date;
    private LocalTime time;

}
