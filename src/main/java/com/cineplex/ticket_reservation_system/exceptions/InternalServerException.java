package com.cineplex.ticket_reservation_system.exceptions;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
}
