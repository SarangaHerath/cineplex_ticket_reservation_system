package com.cineplex.ticket_reservation_system.dto.request;

import com.cineplex.ticket_reservation_system.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RequestUserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Roles roles;
}
