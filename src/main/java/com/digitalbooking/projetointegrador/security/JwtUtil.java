package com.digitalbooking.projetointegrador.security;

import com.digitalbooking.projetointegrador.model.Usuario;
import com.digitalbooking.projetointegrador.security.exception.GenericoTokenException;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import static com.digitalbooking.projetointegrador.security.JwtFiltroValidacao.ATRIBUTO_PREFIXO;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class JwtUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, String> environmentsMap = System.getenv();
    private String senhaAssinaturaToken = environmentsMap.get("SENHA_ASSINATURA_TOKEN");

    private static final int DATA_EXPIRACAO_TOKEN = 1000 * 60 * 60 * 24 * 7;//7dias
    private static final long DATA_EXPIRACAO_REFRESH_TOKEN = 1000L * 60 * 60 * 24 * 30;//30dias
        private static final int TEMPO_EXPIRACAO_TOKEN_VALIDACAO = 1000 * 60 * 15;//15min
    public static final String PERMISSOES = "permissoes";
    public static final String URL_VALIDACAO_REGISTRO = "https://pi-t2-g3.herokuapp.com/usuarios/permitAll/validar-registro/";


    public String gerarToken(Usuario usuario) {
        Claims tokenJwt = Jwts.claims().setSubject(usuario.getUsername());
        tokenJwt.setIssuedAt(new Date());
        tokenJwt.setExpiration(new Date(System.currentTimeMillis() + DATA_EXPIRACAO_TOKEN));
        tokenJwt.put(PERMISSOES,
                usuario.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(tokenJwt)
                .signWith(SignatureAlgorithm.HS256, senhaAssinaturaToken)//assinando o token
                .compact();
    }

    public String gerarRefreshToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getUsername())//email do usuário
                .setIssuedAt(new Date())//quando foi gerado
                .setExpiration(new Date(System.currentTimeMillis() + DATA_EXPIRACAO_REFRESH_TOKEN))//quando expira
                .signWith(SignatureAlgorithm.HS256, senhaAssinaturaToken)//assinando o token
                .compact();
    }

    public String gerarTokenDeValidacaoDeRegistro(Usuario usuario) {
        StringBuilder tokenValidacaoUsuario = new StringBuilder(URL_VALIDACAO_REGISTRO);
        String token = Jwts.builder()
                .setSubject(usuario.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TEMPO_EXPIRACAO_TOKEN_VALIDACAO))
                .signWith(SignatureAlgorithm.HS256, senhaAssinaturaToken)
                .compact();
        tokenValidacaoUsuario.append(usuario.getId());
        tokenValidacaoUsuario.append("/");
        tokenValidacaoUsuario.append(token);
        return tokenValidacaoUsuario.toString();
    }

    public String pegarEmailUsuarioViaToken(String token) {
        if (token.startsWith(ATRIBUTO_PREFIXO))
            token = token.substring(ATRIBUTO_PREFIXO.length());
        Claims claims = Jwts.parser()
                .setSigningKey(senhaAssinaturaToken)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Authentication pegarAtenticacao(String tokenComPrefixoBearer) {
        String token = tokenComPrefixoBearer.substring(ATRIBUTO_PREFIXO.length());//tirando prefixo do token

        Claims claims = Jwts.parser()
                .setSigningKey(senhaAssinaturaToken)
                .parseClaimsJws(token)
                .getBody();
        String usuarioEmail = claims.getSubject();//email do usuário dono do token
        return new UsernamePasswordAuthenticationToken(usuarioEmail, null, getAuthorities(claims));
    }

    public boolean validarToken(String tokenParaValidar) {
        try {
            if (tokenParaValidar.startsWith(ATRIBUTO_PREFIXO))
                tokenParaValidar = tokenParaValidar.substring(ATRIBUTO_PREFIXO.length());
            Jwts.parser().setSigningKey(senhaAssinaturaToken).parseClaimsJws(tokenParaValidar);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String capturarErroEmToken(String tokenComErro) {
        try {
            if (tokenComErro != null)
                if (tokenComErro.startsWith(ATRIBUTO_PREFIXO))
                    tokenComErro = tokenComErro.substring(ATRIBUTO_PREFIXO.length());
            Jwts.parser().setSigningKey(senhaAssinaturaToken).parseClaimsJws(tokenComErro);
            return "";
        } catch (SignatureException e) {
            return "Assinatura de token JWT inválida.";
        } catch (MalformedJwtException e) {
            return "Token inválido.";
        } catch (ExpiredJwtException e) {
            return "Token expirado.";
        } catch (UnsupportedJwtException e) {
            return "Token JWT não suportado.";
        } catch (IllegalArgumentException e) {
            return "A compactação do token JWT do manipulador é inválida.";
        }
    }


    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        if (!map.containsKey(PERMISSOES)) {
            throw new GenericoTokenException("Erro! O token informado não possui as permissões do usuário.");
        }
        Object authorities = map.get(PERMISSOES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                    .collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("As permissões devem ser uma string ou uma coleção");
    }
}
