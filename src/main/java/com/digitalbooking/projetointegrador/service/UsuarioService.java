package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.NovaSenhaDTO;
import com.digitalbooking.projetointegrador.dto.UsuarioDTO;
import com.digitalbooking.projetointegrador.model.Usuario;
import com.digitalbooking.projetointegrador.model.enums.NomeFuncao;
import com.digitalbooking.projetointegrador.repository.IUsuarioRepository;
import com.digitalbooking.projetointegrador.security.JwtUtil;
import com.digitalbooking.projetointegrador.security.exception.GenericoTokenException;
import com.digitalbooking.projetointegrador.security.exception.TokenExpiradoException;
import com.digitalbooking.projetointegrador.service.exception.DadoNaoEncontradoException;
import com.digitalbooking.projetointegrador.service.exception.SenhaIncoretaException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.digitalbooking.projetointegrador.security.JwtFiltroValidacao.ATRIBUTO_PREFIXO;
import static com.digitalbooking.projetointegrador.security.JwtUtil.PERMISSOES;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Classe de service para <strong>Usuario</strong>.
 *
 * @version 1.0
 * @since 1.0
 */
@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private final JwtUtil jwtUtil = new JwtUtil();

    @Autowired
    private EmailService emailService;


    /**
     * Metodo para salvar um produto.
     *
     * @param usuarioDTO Usuario que deve ser persistido no banco de dados.
     * @since 1.0
     */
    public void salvar(UsuarioDTO usuarioDTO) throws MessagingException {
        Usuario usuarioModel = modelMapper.map(usuarioDTO, Usuario.class);
        usuarioModel.setSenha(passwordEncoder.encode(usuarioModel.getSenha()));
        usuarioModel.setUsuarioEstaValidado(false);
        Usuario usuarioSalvo = usuarioRepository.saveAndFlush(usuarioModel);
        String tokenValidacao = jwtUtil.gerarTokenDeValidacaoDeRegistro(usuarioSalvo);//gerando token
        emailService.enviarEmail(emailService.gerarEmailDeValidacao(tokenValidacao, usuarioSalvo.getEmail()));
    }

    /**
     * Metodo que faz a busca de todos os clientes.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Paginacao com usuarios já convertidos de Cliente para ClienteDTO.
     * @since 1.0
     */
    public Page<UsuarioDTO> buscarTodos(Pageable pageable) {
        Page<Usuario> pageUsuarios = usuarioRepository.findAll(pageable);
        return pageUsuarios.map(usuarioModel -> modelMapper.map(usuarioModel, UsuarioDTO.class));
    }

    /**
     * Metodo que busca um usuario pelo email
     *
     * @param username Email a ser buscado
     * @return Usuario que foi encontrado
     * @throws UsernameNotFoundException Excecao lancada caso nao seja encontrado um usuario registrado com o email
     *                                   informado
     * @since 1.0
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(username);
        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException("Usuário com email: " + username + " não encontrado na base de dados.");
        }
        return usuario.get();
    }

    /**
     * Metodo para atualizar um usuario.
     *
     * @param usuarioDTO Usuario a ser atualizado.
     * @since 1.0
     */
    public void atualizar(UsuarioDTO usuarioDTO) {
        Usuario usuarioDaBase =
                usuarioRepository.findById(usuarioDTO.getId()).orElseThrow(() -> new DadoNaoEncontradoException("Usuário " +
                        "não encontrado. Tipo: " + Usuario.class.getName()));
        usuarioDTO.setSenha(usuarioDaBase.getSenha());
        Usuario usuarioModel = modelMapper.map(usuarioDTO, Usuario.class);
        usuarioModel.setUsuarioEstaValidado(true);
        usuarioModel.setDataValidacaoRegistro(usuarioDaBase.getDataValidacaoRegistro());
        if (usuarioDaBase.getFuncao().getNomeFuncaoEnum().equals(NomeFuncao.USER)//evita que um user possa mudar sua função
                && !usuarioDTO.getFuncao().getNomeFuncaoEnum().equals(NomeFuncao.USER)) {
            usuarioModel.setFuncao(usuarioDaBase.getFuncao());
        }
        usuarioRepository.save(usuarioModel);
    }

    /**
     * Metodo para alteracao de senha de um usuario
     *
     * @param novaSenhaDTO Objeto contendo a senha atual que deve ser substituida pela nova senha
     */
    public void atualizarSenha(NovaSenhaDTO novaSenhaDTO) {
        Usuario usuario =
                usuarioRepository.findByEmail(novaSenhaDTO.getEmail()).orElseThrow(() -> new DadoNaoEncontradoException("Falha" +
                        " ao tentar validar registro do usuário com email: " +
                        novaSenhaDTO.getEmail() + ". Usuário não encontrado. Tipo: " + Usuario.class.getName()));
        boolean senhaAtualEstaCorreta = passwordEncoder.matches(novaSenhaDTO.getSenhaAtual(), usuario.getSenha());
        if (senhaAtualEstaCorreta) {
            usuario.setSenha(passwordEncoder.encode(novaSenhaDTO.getSenhaNova()));
            usuarioRepository.save(usuario);
        } else {
            throw new SenhaIncoretaException("Falha ao atualizar senha! A senha atual está incorreta.");
        }
    }

    /**
     * Metodo para alteracao de senha de um usuario
     *
     * @param id    Chave Identificadora do novo usuario gerado pela API e inserida no token enviado no endereco
     *              eletronico cadastrado
     * @param token Token de verificacao de registro do novo usuario gerado pela API e enviado no endereco eletronico
     *              cadastrado
     * @since 1.0
     */
    public String validarRegistro(Long id, String token) throws MessagingException {
        String erroEmToken = jwtUtil.capturarErroEmToken(token);
        Usuario usuario =
                usuarioRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException(
                        "Falha ao tentar validar registro do usuário. Usuário não encontrado. Tipo: " + Usuario.class.getName()));

        if (usuario.isEnabled()) {//usuário já validado
            return "Esse link já foi utilizado anteriormente. Usuário já validado!";
        }

        if (erroEmToken.contains("expirado") && !usuario.isEnabled()) {//token expirado e usuário ainda não validado
            String tokenValidacao = jwtUtil.gerarTokenDeValidacaoDeRegistro(usuario);//gerando token
            emailService.enviarEmail(emailService.gerarEmailDeValidacao(tokenValidacao, usuario.getEmail()));
            return "Link de validação expirado! Confira seu email, em instantes enviaremos um novo link.";
        }
        if (jwtUtil.validarToken(token) && !usuario.isEnabled()) {//token válido e usuário ainda não validado
            String emailDoUsuario = jwtUtil.pegarEmailUsuarioViaToken(token);
            Usuario usuarioDoToken = usuarioRepository.findByEmail(emailDoUsuario).orElseThrow(() -> new DadoNaoEncontradoException(
                    "Falha ao tentar validar registro do usuário. Usuário não encontrado. Tipo: " + Usuario.class.getName()));
            if (usuario.getId().equals(usuarioDoToken.getId())) {//valida se o token realmente pertence ao usuário
                usuario.setUsuarioEstaValidado(true);
                usuario.setDataValidacaoRegistro(new Date());
                usuarioRepository.save(usuario);//validando usuário
                return "Novo usuário validado com sucesso!";
            } else {
                return "Link de validação inválido! O link informado viola regras de segurança.";
            }
        } else {//para outros tipos de possíveis erros
            return erroEmToken;
        }
    }

    /**
     * Metodo para alteracao do token JWT de um usuario
     *
     * @param request  Requisicao do cliente e contem as informacoes ao seu respeito
     * @param response Resposta Servlet enviada ao cliente
     * @since 1.0
     */
    public void atualizarToken(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);//token com o prefixo Bearer
        if (authorizationHeader != null && authorizationHeader.startsWith(ATRIBUTO_PREFIXO)) {
            if (jwtUtil.validarToken(authorizationHeader)) {
                try {
                    String emailDoUsuario = jwtUtil.pegarEmailUsuarioViaToken(authorizationHeader);
                    Usuario usuario =
                            usuarioRepository.findByEmail(emailDoUsuario).orElseThrow(() -> new DadoNaoEncontradoException("Falha ao tentar atualizar token JWT do usuário com email: " +
                                    emailDoUsuario + ". Usuário não encontrado. Tipo: " + Usuario.class.getName()));

                    String token = jwtUtil.gerarToken(usuario);//criando token
                    String refreshTokenAtualizado = jwtUtil.gerarRefreshToken(usuario);//criando refresh token

                    Map<String, Object> data = new HashMap<>();
                    data.put("token", token);
                    data.put("refresh_token", refreshTokenAtualizado);
                    data.put(PERMISSOES,
                            usuario.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
                    data.put("funcao", usuario.getFuncao().getNomeFuncaoEnum());

                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), data);//escrevendo o objeto data na response
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new TokenExpiradoException("Refresh token expirado.");
            }
        } else {
            throw new GenericoTokenException("Falha ao atualizar token JWT. Informe um valor válido para o " +
                    "'refresh-token' e verifique a existência do prefixo 'Bearer ' em sua requisição.");
        }
    }

    /**
     * Metodo para buscar usuario via token JWT
     *
     * @param request Requisicao do cliente e contem as informacoes ao seu respeito
     * @since 1.0
     */
    public UsuarioDTO buscarUsuarioViaJWT(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);//token com o prefixo Bearer
        if (authorizationHeader != null && authorizationHeader.startsWith(ATRIBUTO_PREFIXO)) {
            if (jwtUtil.validarToken(authorizationHeader)) {
                String emailDoUsuario = jwtUtil.pegarEmailUsuarioViaToken(authorizationHeader);
                Usuario usuario =
                        usuarioRepository.findByEmail(emailDoUsuario).orElseThrow(() -> new DadoNaoEncontradoException("Falha ao buscar usuário via token JWT." +
                                " Usuário não encontrado. Tipo: " + Usuario.class.getName()));
                return modelMapper.map(usuario, UsuarioDTO.class);
            } else {
                throw new GenericoTokenException("Erro ao tentar bucar usuário via token JWT");
            }
        }
        return null;
    }

    /**
     * Metodo para deletar um usuario.
     *
     * @param id Chave identificadora do usuario que deve ser deletado.
     * @since 1.0
     */
    public void deletar(Long id) {
        usuarioRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException("Usuário não " +
                "encontrado. Tipo: " + Usuario.class.getName()));
        usuarioRepository.deleteById(id);
    }

}
