package com.cineplex.ticket_reservation_system.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RequestMovieDto {

    private Long movieId;
    private String movieName;
    private String movieDescription;
    private List<RequestShowTimeDto> requestShowTimeDtoList;

}
