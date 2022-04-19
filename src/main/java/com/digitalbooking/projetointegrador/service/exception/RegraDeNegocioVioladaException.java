package com.digitalbooking.projetointegrador.service.exception;

public class RegraDeNegocioVioladaException extends RuntimeException {
    public RegraDeNegocioVioladaException() {
        super();
    }

    public RegraDeNegocioVioladaException(String message) {
        super(message);
    }

    public RegraDeNegocioVioladaException(String message, Throwable cause) {
        super(message, cause);
    }
}
