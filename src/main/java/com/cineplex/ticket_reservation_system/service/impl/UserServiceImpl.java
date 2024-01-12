package com.cineplex.ticket_reservation_system.service.impl;

import com.cineplex.ticket_reservation_system.dto.request.RequestUserDto;
import com.cineplex.ticket_reservation_system.dto.response.CommonResponse;
import com.cineplex.ticket_reservation_system.dto.response.ResponseUserDto;
import com.cineplex.ticket_reservation_system.entity.User;
import com.cineplex.ticket_reservation_system.exceptions.InternalServerException;
import com.cineplex.ticket_reservation_system.exceptions.ResourceNotFoundException;
import com.cineplex.ticket_reservation_system.repository.UserRepo;
import com.cineplex.ticket_reservation_system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public ResponseEntity<CommonResponse> getAllUsers() {
        try {
            log.info("hit get all method serviceImpl");
            List<ResponseUserDto> userDtoList = userRepo.findAll()
                    .stream()
                    .map(user -> ResponseUserDto.builder()
                            .userId(user.getUserId())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .userName(user.getUsername())
                            .build())
                    .toList();
            return ResponseEntity.ok(CommonResponse.builder()
                    .data(userDtoList)
                    .message("Retrieved all users successfully")
                    .responseCode(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            log.error("failed to get all data !");
            throw new InternalServerException("All customers retrieved failed !");
        }

    }

    @Override
    public ResponseEntity<CommonResponse> updateUser(RequestUserDto requestUserDto) {
        try {
            log.info("hit update user method serviceImpl");
            Optional<User> userOptional = userRepo.findById(requestUserDto.getUserId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setFirstName(requestUserDto.getFirstName());
                user.setLastName(requestUserDto.getLastName());
                user.setUsername(requestUserDto.getUsername());
                user.setPassword(requestUserDto.getPassword());

                userRepo.save(user);

                ResponseUserDto responseUserDto = ResponseUserDto.builder()
                        .userId(user.getUserId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .userName(user.getUsername())
                        .password(user.getPassword())
                        .build();
                return ResponseEntity.ok(CommonResponse.builder()
                        .responseCode(HttpStatus.OK)
                        .message("User updated successful")
                        .data(responseUserDto)
                        .build());
            } else {
                log.error("Can't find user id : {}", requestUserDto.getUserId());
                throw new ResourceNotFoundException("Can't find user");
            }
        } catch (Exception e) {
            log.error("Failed to update user id : {}", requestUserDto.getUserId());
            throw new InternalServerException("Failed to update user");
        }


    }

    @Override
    public ResponseEntity<CommonResponse> deleteUser(Long id) {
        try {
            log.info("hit delete method serviceImpl");
            Optional<User> userOptional = userRepo.findById(id);
            if (userOptional.isPresent()) {
                userRepo.deleteById(id);
                return ResponseEntity.ok(CommonResponse.builder()
                        .message("User deleted success")
                        .responseCode(HttpStatus.OK)
                        .build());
            } else {
                log.error("Can't find user userId :{}", id);
                throw new ResourceNotFoundException("Can't find user");
            }

        } catch (Exception e) {
            log.error("failed to delete user userId : {}", id);
            throw new InternalServerException("Error occur during delete user");
        }
    }

    @Override
    public ResponseEntity<CommonResponse> findUserById(Long id) {
        try {
            log.info("hit find user by id serviceImpl");
            Optional<User> optionalUser = userRepo.findById(id);
            if (optionalUser.isPresent()) {
                User findUser = optionalUser.get();
                ResponseUserDto responseUserDto = ResponseUserDto.builder()
                        .userId(findUser.getUserId())
                        .firstName(findUser.getFirstName())
                        .lastName(findUser.getLastName())
                        .userName(findUser.getUsername())
                        .build();
                return ResponseEntity.ok(CommonResponse.builder()
                        .message("Successful find user")
                        .responseCode(HttpStatus.OK)
                        .data(responseUserDto)
                        .build());
            } else {
                log.error("Can't find user userId :{}", id);
                throw new ResourceNotFoundException("Can't find user");
            }
        } catch (Exception e) {
            log.error("failed to find user userId : {}", id);
            throw new InternalServerException("Error occur during find user");
        }
    }
}
