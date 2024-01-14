package com.cineplex.ticket_reservation_system.dto.response;

import com.cineplex.ticket_reservation_system.entity.ShowTime;
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
    private ResponseMovieDto responseMovieDto;

    // Method to populate ResponseShowTimeDto from ShowTime entity
    public static ResponseShowTimeDto fromShowTimeEntity(ShowTime showTime) {
        return ResponseShowTimeDto.builder()
                .showTimeId(showTime.getShowTimeId())
                .time(showTime.getTime())
                .availableSeats(showTime.getAvailableSeats())
                .responseMovieDto(ResponseMovieDto.fromMovieEntity(showTime.getMovie()))
                .build();
    }
}
