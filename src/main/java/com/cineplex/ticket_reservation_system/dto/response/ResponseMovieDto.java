package com.cineplex.ticket_reservation_system.dto.response;

import com.cineplex.ticket_reservation_system.entity.Movie;
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

    // Method to populate RequestMovieDto from Movie entity
    public static ResponseMovieDto fromMovieEntity(Movie movie) {
        return ResponseMovieDto.builder()
                .movieId(movie.getMovieId())
                .movieName(movie.getMovieName())
                .movieDescription(movie.getMovieDescription())
                .build();
    }
}
