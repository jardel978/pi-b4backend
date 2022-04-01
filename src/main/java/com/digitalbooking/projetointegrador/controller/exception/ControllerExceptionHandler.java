package com.digitalbooking.projetointegrador.controller.exception;

import com.digitalbooking.projetointegrador.controller.HandlerError;
import com.digitalbooking.projetointegrador.security.exception.GenericoTokenException;
import com.digitalbooking.projetointegrador.security.exception.TokenExpiradoException;
import com.digitalbooking.projetointegrador.service.exception.DadoNaoEncontradoException;
import com.digitalbooking.projetointegrador.service.exception.DadoRelacionadoException;
import com.digitalbooking.projetointegrador.service.exception.SenhaIncoretaException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Classe configurada para captura de erros e suas manipulacoes.
 *
 * @version 1.0
 * @since 1.07
 */

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Metodo que para tratar excecoes do tipo: DadoNaoEncontradoException.
     *
     * @param e       Excecao que foi interceptada.
     * @param request Requisicao do cliente e contem as informacoes ao seu respeito.
     * @return Response HTTP personalizada com HttpStatus 403 e um objeto HandlerError contendo informacoes do erro
     * ocorrido.
     * @since 1.0
     */
    @ExceptionHandler(DadoNaoEncontradoException.class)
    public ResponseEntity<HandlerError> dadoNaoEncontradoException(DadoNaoEncontradoException e, HttpServletRequest request) {
        HandlerError error = HandlerError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .mensagem(e.getMessage())
                .data(new Date())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Metodo que para tratar excecoes do tipo: CampoInvalidoException.
     *
     * @param e       Excecao que foi interceptada.
     * @param request Requisicao do cliente e contem as informacoes ao seu respeito.
     * @return Response HTTP personalizada com HttpStatus 400 e um objeto HandlerError contendo informacoes do erro
     * ocorrido.
     * @since 1.0
     */
    @ExceptionHandler(CampoInvalidoException.class)
    public ResponseEntity<HandlerError> campoInvalidoException(CampoInvalidoException e, HttpServletRequest request) {
        HandlerError error = HandlerError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .mensagem(e.getMessage())
                .data(new Date())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Metodo que para tratar excecoes do tipo: DataIntegrityViolationException.
     *
     * @param e       Excecao que foi interceptada.
     * @param request Requisicao do cliente e contem as informacoes ao seu respeito.
     * @return Response HTTP personalizada com HttpStatus 400 e um objeto HandlerError contendo informacoes do erro
     * ocorrido.
     * @since 1.0
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<HandlerError> dataIntegrityViolationException(DataIntegrityViolationException e,
                                                                        HttpServletRequest request) {

        //pegando a tabela.campo inválido informado na mensagem da excecao
        String campoInvalido = e.getMessage().substring(
                e.getMessage().lastIndexOf("[tb_") + 4, e.getMessage().lastIndexOf("_UNIQUE"));

        HandlerError error = HandlerError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .mensagem(e.getMessage().contains("UNIQUE") ? "Você está tentando salvar um dado que já existe no " +
                        "banco de dados." + " Campo inválido: " + campoInvalido
                        :
                        e.getMessage())
                .data(new Date())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Metodo que para tratar excecoes do tipo: DadoRelacionadoException.
     *
     * @param e       Excecao que foi interceptada.
     * @param request Requisicao do cliente e contem as informacoes ao seu respeito.
     * @return Response HTTP personalizada com HttpStatus 400 e um objeto HandlerError contendo informacoes do erro
     * ocorrido.
     * @since 1.0
     */
    @ExceptionHandler(DadoRelacionadoException.class)
    public ResponseEntity<HandlerError> dadoRelacionadoException(DadoRelacionadoException e,
                                                                 HttpServletRequest request) {

        HandlerError error = HandlerError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .mensagem(e.getMessage())
                .data(new Date())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Metodo que para tratar excecoes do tipo: UsernameNotFoundException.
     *
     * @param e       Excecao que foi interceptada.
     * @param request Requisicao do cliente e contem as informacoes ao seu respeito.
     * @return Response HTTP personalizada com HttpStatus 400 e um objeto HandlerError contendo informacoes do erro
     * ocorrido.
     * @since 1.0
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<HandlerError> usernameNotFoundException(UsernameNotFoundException e,
                                                                  HttpServletRequest request) {
        HandlerError error = HandlerError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .mensagem(e.getMessage())
                .data(new Date())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Metodo que para tratar excecoes do tipo: UsernameNotFoundException.
     *
     * @param e       Excecao que foi interceptada.
     * @param request Requisicao do cliente e contem as informacoes ao seu respeito.
     * @return Response HTTP personalizada com HttpStatus 400 e um objeto HandlerError contendo informacoes do erro
     * ocorrido.
     * @since 1.0
     */
    @ExceptionHandler(TokenExpiradoException.class)
    public ResponseEntity<HandlerError> tokenExpiradoException(TokenExpiradoException e,
                                                               HttpServletRequest request) {
        HandlerError error = HandlerError.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .mensagem(e.getMessage())
                .data(new Date())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Metodo que para tratar excecoes do tipo: GenericoTokenException.
     *
     * @param e       Excecao que foi interceptada.
     * @param request Requisicao do cliente e contem as informacoes ao seu respeito.
     * @return Response HTTP personalizada com HttpStatus 400 e um objeto HandlerError contendo informacoes do erro
     * ocorrido.
     * @since 1.0
     */
    @ExceptionHandler(GenericoTokenException.class)
    public ResponseEntity<HandlerError> genericoTokenException(GenericoTokenException e,
                                                               HttpServletRequest request) {
        HandlerError error = HandlerError.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .mensagem(e.getMessage())
                .data(new Date())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Metodo que para tratar excecoes do tipo: SenhaIncoretaException.
     *
     * @param e       Excecao que foi interceptada.
     * @param request Requisicao do cliente e contem as informacoes ao seu respeito.
     * @return Response HTTP personalizada com HttpStatus 400 e um objeto HandlerError contendo informacoes do erro
     * ocorrido.
     * @since 1.0
     */
    @ExceptionHandler(SenhaIncoretaException.class)
    public ResponseEntity<HandlerError> senhaIncoretaException(SenhaIncoretaException e,
                                                               HttpServletRequest request) {
        HandlerError error = HandlerError.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .mensagem(e.getMessage())
                .data(new Date())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

}
