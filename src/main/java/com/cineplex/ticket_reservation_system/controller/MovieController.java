package com.cineplex.ticket_reservation_system.controller;

import com.cineplex.ticket_reservation_system.dto.request.RequestMovieDto;
import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import com.cineplex.ticket_reservation_system.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/movie")
@Slf4j
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/save")
    public ResponseEntity<CommonResponse> saveMovie(@RequestBody RequestMovieDto requestMovieDto) {
        log.info("hit movie save dto:{}", requestMovieDto);
        return movieService.saveMovie(requestMovieDto);
    }

    @PutMapping("/update")
    public ResponseEntity<CommonResponse> updateMovie(@RequestBody RequestMovieDto requestMovieDto) {
        log.info("hit movie update dto:{}", requestMovieDto);
        return movieService.updateMovie(requestMovieDto);
    }

    @GetMapping("/getAll")
    public ResponseEntity<CommonResponse> getAllMovie() {
        log.info("hit get all movie method");
        return movieService.getAllMovie();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<CommonResponse> getMovieById(@PathVariable Long id) {
        log.info("hit get movie by id method");
        return movieService.getMovieById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse> deleteMovie(@PathVariable Long id) {
        log.info("hit delete movie method");
        return movieService.deleteMovie(id);
    }
}
