package com.cineplex.ticket_reservation_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class RequestChangeStatusDto {
    private Long reservationId;
    private String status;
}
