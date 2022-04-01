package com.digitalbooking.projetointegrador.security.exception;


/**
 * Criacao de excecao personalizada para tratamento de erros por token expirado.
 *
 * @version 1.0
 * @since 1.0
 */
public class GenericoTokenException extends RuntimeException {

    public GenericoTokenException() {
        super();
    }

    public GenericoTokenException(String message) {
        super(message);
    }

    public GenericoTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
