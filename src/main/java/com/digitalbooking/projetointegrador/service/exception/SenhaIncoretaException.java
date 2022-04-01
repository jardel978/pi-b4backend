package com.digitalbooking.projetointegrador.service.exception;

public class SenhaIncoretaException extends RuntimeException {
    public SenhaIncoretaException() {
        super();
    }

    public SenhaIncoretaException(String message) {
        super(message);
    }

    public SenhaIncoretaException(String message, Throwable cause) {
        super(message, cause);
    }
}
