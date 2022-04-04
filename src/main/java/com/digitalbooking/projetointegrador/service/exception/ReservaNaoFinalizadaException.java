package com.digitalbooking.projetointegrador.service.exception;

public class ReservaNaoFinalizadaException extends RuntimeException {
    public ReservaNaoFinalizadaException() {
        super();
    }

    public ReservaNaoFinalizadaException(String message) {
        super(message);
    }

    public ReservaNaoFinalizadaException(String message, Throwable cause) {
        super(message, cause);
    }
}
