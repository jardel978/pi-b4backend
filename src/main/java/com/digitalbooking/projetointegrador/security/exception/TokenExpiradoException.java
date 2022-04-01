package com.digitalbooking.projetointegrador.security.exception;


/**
 * Criacao de excecao personalizada para tratamento de erros por token expirado.
 *
 * @version 1.0
 * @since 1.0
 */
public class TokenExpiradoException extends RuntimeException {

    public TokenExpiradoException() {
        super();
    }

    public TokenExpiradoException(String message) {
        super(message);
    }

    public TokenExpiradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
