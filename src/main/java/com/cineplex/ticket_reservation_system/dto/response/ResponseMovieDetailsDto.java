package com.cineplex.ticket_reservation_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseMovieDetailsDto {
    private ResponseMovieDto responseMovieDto;
    private List<ResponseShowTimeDto> responseShowTimeDtoList;

}
