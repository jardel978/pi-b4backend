package com.digitalbooking.projetointegrador.service.exception;

public class DadoRelacionadoException extends RuntimeException {
    public DadoRelacionadoException(String message) {
        super(message);
    }

    public DadoRelacionadoException(String message, Throwable cause) {
        super(message, cause);
    }
}
