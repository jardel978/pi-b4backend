package com.digitalbooking.projetointegrador.security;

import com.digitalbooking.projetointegrador.controller.HandlerError;
import com.digitalbooking.projetointegrador.dto.CredenciaisParaLogin;
import com.digitalbooking.projetointegrador.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.digitalbooking.projetointegrador.security.JwtUtil.PERMISSOES;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@NoArgsConstructor
public class JwtFiltroAutenticacao extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private final JwtUtil jwtUtil = new JwtUtil();

    public JwtFiltroAutenticacao(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            CredenciaisParaLogin credenciais = new ObjectMapper().readValue(request.getInputStream(), CredenciaisParaLogin.class);
            return super.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                    credenciais.getEmail(),
                    credenciais.getSenha()
            ));
        } catch (AuthenticationException e) {
            throw new AuthenticationCredentialsNotFoundException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Usuario usuario = (Usuario) authResult.getPrincipal();
        String token = jwtUtil.gerarToken(usuario);//criando token
        String refreshToken = jwtUtil.gerarRefreshToken(usuario);//criando refresh token

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("refresh_token", refreshToken);
        data.put(PERMISSOES,
                usuario.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        data.put("funcao", usuario.getFuncao().getNomeFuncaoEnum());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), data);//escrevendo o objeto data na response
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException e) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//setando status 401
        response.setHeader("error", "Erro ao realizar login");
        HandlerError error = HandlerError
                .builder()
                .status(UNAUTHORIZED.value())
                .mensagem(e.getMessage().contains("disabled") ?
                        "Falha na autenticação. Usuário cadastrado, porém ainda não validou o seu registro. " +
                                "Por favor, acesse o link que enviamos ao seu email e valide seu cadastro!"
                        : "Falha na autenticação. Verifique os dados de acesso do usuário")
                .data(new Date())
                .path(request.getRequestURI())
                .build();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(error));//escrevendo erro na resposta
    }
}
