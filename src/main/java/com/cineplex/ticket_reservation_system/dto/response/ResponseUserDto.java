package com.cineplex.ticket_reservation_system.dto.response;

import com.cineplex.ticket_reservation_system.entity.Roles;
import com.cineplex.ticket_reservation_system.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseUserDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Roles roles;

    // Method to populate ResponseUserDto from User entity
    public static ResponseUserDto fromUserEntity(User user) {
        return ResponseUserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }

}
