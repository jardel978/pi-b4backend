package com.digitalbooking.projetointegrador.service.exception;

public class DataIndisponivelReservaException extends RuntimeException {
    public DataIndisponivelReservaException() {
        super();
    }

    public DataIndisponivelReservaException(String message) {
        super(message);
    }

    public DataIndisponivelReservaException(String message, Throwable cause) {
        super(message, cause);
    }
}
