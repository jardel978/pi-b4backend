package com.digitalbooking.projetointegrador.service.exception;

public class RegraDeSegurancaVioladaException extends RuntimeException {
    public RegraDeSegurancaVioladaException() {
        super();
    }

    public RegraDeSegurancaVioladaException(String message) {
        super(message);
    }

    public RegraDeSegurancaVioladaException(String message, Throwable cause) {
        super(message, cause);
    }
}
