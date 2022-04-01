package com.digitalbooking.projetointegrador.security;

import com.digitalbooking.projetointegrador.controller.HandlerError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(403);

        HandlerError error = HandlerError.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .mensagem("Acesso negado. O usuário não possui acesso ao recurso.")
                .data(new Date())
                .path(request.getRequestURI())
                .build();
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(error));
    }

}