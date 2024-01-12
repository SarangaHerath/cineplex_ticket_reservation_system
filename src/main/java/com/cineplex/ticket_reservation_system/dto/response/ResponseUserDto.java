package com.cineplex.ticket_reservation_system.dto.response;

import com.cineplex.ticket_reservation_system.entity.Roles;
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

}
