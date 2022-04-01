package com.digitalbooking.projetointegrador.controller.exception;


/**
 * Criacao de excecao personalizada para tratamento de erros na validação de dados.
 *
 * @version 1.0
 * @since 1.0
 */

public class CampoInvalidoException extends RuntimeException {

    public CampoInvalidoException() {
    }

    public CampoInvalidoException(String message) {
        super(message);
    }

    public CampoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
