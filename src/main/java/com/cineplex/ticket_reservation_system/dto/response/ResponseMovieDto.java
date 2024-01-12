package com.cineplex.ticket_reservation_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseMovieDto {
    private Long movieId;
    private String movieName;
    private String movieDescription;
}
