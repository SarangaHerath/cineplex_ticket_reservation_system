package com.cineplex.ticket_reservation_system.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public class CommonResponse {
    public final Object data;
    public final String message;
    public final HttpStatus responseCode;
}
