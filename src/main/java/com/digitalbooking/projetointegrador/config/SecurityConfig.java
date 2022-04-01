package com.digitalbooking.projetointegrador.config;

import com.digitalbooking.projetointegrador.security.CustomAccessDeniedHandler;
import com.digitalbooking.projetointegrador.security.CustomAuthenticationEntryPoint;
import com.digitalbooking.projetointegrador.security.JwtFiltroAutenticacao;
import com.digitalbooking.projetointegrador.security.JwtFiltroValidacao;
import com.digitalbooking.projetointegrador.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;

@Component
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] ROTAS_PERMIT_ALL = {"/login/**", "/usuarios/permitAll/**", "/produtos/permitAll/**"
            , "/categorias/permitAll/**", "/cidades/permitAll/**", "/clientes/permitAll/**", "/v3/api-docs/**",
            "/swagger-ui/**", "/swagger-ui.html", "/webjars/swagger-ui/**"};

    private static final String[] ROLES_USER = {"ROLE_USER"};
    private static final String[] ROLES_ADMIN = {"ROLE_ADMIN"};

    private static final String[] ROTAS_USER = {"/reservas/cliente/**", "/clientes/**", "/usuarios/atualizar/alterar" +
            "-senha"};

    private static final String[] ROTAS_ADMIN = {"/usuarios/**", "/reservas/**", "/produtos/**", "/imagens/**",
            "/funcoes/**", "/clientes/**", "/cidades/**", "/categorias/**", "/caracteristicas/**"};

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(ROTAS_PERMIT_ALL).permitAll();

        //acessos a rotas para usuários
        http.authorizeRequests().antMatchers(GET, ROTAS_USER).hasAnyAuthority(ROLES_USER);
        http.authorizeRequests().antMatchers(POST, ROTAS_USER).hasAnyAuthority(ROLES_USER);
        http.authorizeRequests().antMatchers(PUT, ROTAS_USER).hasAnyAuthority(ROLES_USER);
        http.authorizeRequests().antMatchers(DELETE, ROTAS_USER).hasAnyAuthority(ROLES_USER);

        //acessos a rotas para administradores
        http.authorizeRequests().antMatchers(GET, ROTAS_ADMIN).hasAnyAuthority(ROLES_ADMIN);
        http.authorizeRequests().antMatchers(POST, ROTAS_ADMIN).hasAnyAuthority(ROLES_ADMIN);
        http.authorizeRequests().antMatchers(PUT, ROTAS_ADMIN).hasAnyAuthority(ROLES_ADMIN);
        http.authorizeRequests().antMatchers(DELETE, ROTAS_ADMIN).hasAnyAuthority(ROLES_ADMIN);

        http.authorizeRequests().anyRequest().authenticated();
//        http.formLogin();
        http.addFilter(new JwtFiltroAutenticacao(authenticationManagerBean()));
        http.addFilterBefore(new JwtFiltroValidacao(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);//para erros na autenticação
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);//para erros na validação

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
