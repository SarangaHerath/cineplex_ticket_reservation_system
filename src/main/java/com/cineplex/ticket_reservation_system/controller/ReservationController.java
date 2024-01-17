package com.cineplex.ticket_reservation_system.controller;

import com.cineplex.ticket_reservation_system.dto.request.RequestReservationDto;
import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import com.cineplex.ticket_reservation_system.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("api/v1/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/save")
    public ResponseEntity<CommonResponse> saveReservation(@RequestBody RequestReservationDto requestReservationDto) {
        log.info("hit saveReservation controller dto :{}", requestReservationDto);
        return reservationService.saveReservation(requestReservationDto);
    }

    @PutMapping("/update")
    public ResponseEntity<CommonResponse> updateReservation(@RequestBody RequestReservationDto requestReservationDto) {
        log.info("hit updateReservation controller dto :{}", requestReservationDto);
        return reservationService.updateReservation(requestReservationDto);
    }

    @GetMapping("/getAll")
    public ResponseEntity<CommonResponse> getAllReservation() {
        log.info("hit updateReservation controller");
        return reservationService.getAllReservation();
    }

    @GetMapping("/getReservationById/{id}")
    public ResponseEntity<CommonResponse> getReservationById(@PathVariable Long id) {
        log.info("hit getReservationById controller");
        return reservationService.getReservationById(id);
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<CommonResponse> cancelReservation(@PathVariable Long id) {
        log.info("hit cancelReservation controller");
        return reservationService.cancelReservation(id);
    }
//    @PostMapping("/changeStatus")
//    public ResponseEntity<CommonResponse> changeStatus(@RequestBody RequestChangeStatusDto requestChangeStatusDto){
//        log.info("hit cancelReservation controller");
//        return reservationService.changeStatus(requestChangeStatusDto);
//    }

    @GetMapping("getReservationByUserId/{id}")
    public ResponseEntity<CommonResponse> getReservationByUserId(@PathVariable Long id) {
        log.info("hit getReservationByUserName controller");
        return reservationService.getReservationByUserId(id);
    }
}
