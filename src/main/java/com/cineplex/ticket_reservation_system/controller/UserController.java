package com.cineplex.ticket_reservation_system.controller;

import com.cineplex.ticket_reservation_system.dto.request.RequestUserDto;
import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import com.cineplex.ticket_reservation_system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/user")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<CommonResponse> getAllUsers() {
        log.info("hit get all user method");
        return userService.getAllUsers();
    }

    @PutMapping("/update")
    public ResponseEntity<CommonResponse> updateUser(@RequestBody RequestUserDto requestUserDto) {
        log.info("hit update user: dto {}", requestUserDto);
        return userService.updateUser(requestUserDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse> deleteUser(@PathVariable Long id) {
        log.info("hit delete user: id {}", id);
        return userService.deleteUser(id);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<CommonResponse> findUserById(@PathVariable Long id) {
        log.info("hit find user by id user: id {}", id);
        return userService.findUserById(id);
    }

}

