package com.cineplex.ticket_reservation_system.dto.response;

import com.cineplex.ticket_reservation_system.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseShowTimeDto {
    private Long showTimeId;
    private LocalTime time;
    private int availableSeats;
    private Movie movie;
}
