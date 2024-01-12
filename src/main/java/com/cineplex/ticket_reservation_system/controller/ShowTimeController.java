package com.cineplex.ticket_reservation_system.controller;

import com.cineplex.ticket_reservation_system.dto.request.RequestMovieDto;
import com.cineplex.ticket_reservation_system.dto.request.RequestShowTimeDto;
import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import com.cineplex.ticket_reservation_system.service.ShowTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("api/v1/showTime")
public class ShowTimeController {

    
    private final ShowTimeService showTimeService;

    public ShowTimeController(ShowTimeService showTimeService) {
        this.showTimeService = showTimeService;
    }

    @PostMapping("/save")
    public ResponseEntity<CommonResponse> saveShowTime(@RequestBody RequestShowTimeDto requestShowTimeDto) {
        log.info("hit movie save dto:{}", requestShowTimeDto);
        return showTimeService.saveShowTime(requestShowTimeDto);
    }

    @PutMapping("/update")
    public ResponseEntity<CommonResponse> updateShowTime(@RequestBody RequestShowTimeDto requestShowTimeDto) {
        log.info("hit movie update dto:{}", requestShowTimeDto);
        return showTimeService.updateShowTime(requestShowTimeDto);
    }

    @GetMapping("/getAll")
    public ResponseEntity<CommonResponse> getAllMovie() {
        log.info("hit get all movie method");
        return showTimeService.getAllShowTime();
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<CommonResponse> getMovieById(@PathVariable Long id) {
        log.info("hit get movie by id method");
        return showTimeService.getShowTimeById(id);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse> deleteMovie(@PathVariable Long id) {
        log.info("hit delete movie method");
        return showTimeService.deleteShowTime(id);
    }
}