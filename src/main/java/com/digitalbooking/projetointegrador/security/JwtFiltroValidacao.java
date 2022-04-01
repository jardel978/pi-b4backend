package com.digitalbooking.projetointegrador.security;

import com.digitalbooking.projetointegrador.controller.HandlerError;
import com.digitalbooking.projetointegrador.security.exception.GenericoTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class JwtFiltroValidacao extends OncePerRequestFilter {

    public static final String ATRIBUTO_PREFIXO = "Bearer ";

    @Autowired
    private final JwtUtil jwtUtil = new JwtUtil();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/login")
                || request.getServletPath().contains("/permitAll")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);//token com o prefixo Bearer
            if (jwtUtil.validarToken(authorizationHeader)) {
                if (authorizationHeader != null && authorizationHeader.startsWith(ATRIBUTO_PREFIXO)) {
                    try {
                        SecurityContextHolder.getContext().setAuthentication(
                                jwtUtil.pegarAtenticacao(authorizationHeader));
                        filterChain.doFilter(request, response);
                    } catch (Exception e) {
                        throw new GenericoTokenException("Erro! Token inválido ou não informado.");
                    }
                } else {
                    filterChain.doFilter(request, response);
                }
            } else {
                String erroEmToken = jwtUtil.capturarErroEmToken(authorizationHeader);
                response.setHeader("error", erroEmToken);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//setando status 401
                HandlerError error = HandlerError
                        .builder()
                        .status(UNAUTHORIZED.value())
                        .mensagem(erroEmToken)
                        .data(new Date())
                        .path(request.getRequestURI())
                        .build();

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                ObjectMapper mapper = new ObjectMapper();
                response.getWriter().write(mapper.writeValueAsString(error));
            }
        }
    }
}
