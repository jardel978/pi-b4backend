package com.digitalbooking.projetointegrador.security;

import com.digitalbooking.projetointegrador.controller.HandlerError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException
            , ServletException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(403);

        HandlerError error = HandlerError.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .mensagem(e.getMessage())
                .data(new Date())
                .path(request.getRequestURI())
                .build();
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(error));
    }
}