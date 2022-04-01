package com.digitalbooking.projetointegrador.service.exception;

/**
 * Criacao de excecao personalizada para tratamento de erros por dados nao localizados na base de dados.
 *
 * @version 1.0
 * @since 1.0
 */

public class DadoNaoEncontradoException extends RuntimeException {

    public DadoNaoEncontradoException(String message) {
        super(message);
    }

    public DadoNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
