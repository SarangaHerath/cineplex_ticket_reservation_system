package com.cineplex.ticket_reservation_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RequestShowTimeDto {

    private Long ShowTimeId;
    private Long movieId;
    private LocalTime time;

}
