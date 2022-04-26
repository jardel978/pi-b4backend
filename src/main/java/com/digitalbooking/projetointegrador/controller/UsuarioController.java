package com.digitalbooking.projetointegrador.controller;

import com.digitalbooking.projetointegrador.controller.exception.CampoInvalidoException;
import com.digitalbooking.projetointegrador.dto.ClienteDTO;
import com.digitalbooking.projetointegrador.dto.NovaSenhaDTO;
import com.digitalbooking.projetointegrador.dto.UsuarioDTO;
import com.digitalbooking.projetointegrador.service.ClienteService;
import com.digitalbooking.projetointegrador.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Classe de controller para <strong>Usuario</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@RestController
@RequestMapping("/usuarios")
//SpringDoc documentação
@Tag(name = "Usuários", description = "API REST Usuários")
@SecurityRequirement(name = "apidigitalbooking")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClienteService clienteService;

    /**
     * Metodo para salvar um usuario.
     *
     * @param usuarioDTO Usuario que deve ser persistido no banco de dados.
     * @param bdResult   Interface geral para validação de dados.
     * @return Response HTTP personalizada com HttpStatus 201.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Salva um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário salvo", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(@Parameter(description = "Usuário a ser salvo na base de dados") @Valid @RequestBody UsuarioDTO usuarioDTO,
                                    @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) throws MessagingException {
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        usuarioService.salvar(usuarioDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Metodo que faz a busca de todos os usuarios.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Paginacao com usuarios já convertidos de Usuario para UsuarioDTO.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Retorna todos os usuários paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários encontrados"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção", content = @Content(schema =
            @Schema(implementation = HandlerError.class))),
    })
    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> buscarTodos(@ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarTodos(pageable));
    }

    /**
     * Metodo que faz a busca de todos os clientes.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Paginacao com clientes já convertidos de Cliente para ClienteDTO.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Retorna todos os clientes paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção", content = @Content(schema =
            @Schema(implementation = HandlerError.class))),
    })
    @GetMapping("/buscar-clientes")
    public ResponseEntity<Page<ClienteDTO>> buscarTodosClientes(@ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.buscarTodos(pageable));
    }

    /**
     * Metodo responsavel por atualizar um produto.
     *
     * @param usuarioDTO Usuário que deve ser atualizado.
     * @param bdResult   Interface geral para validação de dados.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Atualiza um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não localizado na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizar(@Parameter(description = "Usuário a ser atualizado na base de dados") @Valid @RequestBody UsuarioDTO usuarioDTO,
                                       @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) {
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        usuarioService.atualizar(usuarioDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Metodo para alteracao do token JWT de um usuario
     *
     * @param request  Requisicao do cliente e contem as informacoes ao seu respeito
     * @param response Resposta Servlet enviada ao cliente
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Atualiza token JWT do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token JWT atualizado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não localizado na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @GetMapping("/permitAll/refresh-token")
    public void atualizarToken(HttpServletRequest request, HttpServletResponse response) {
        usuarioService.atualizarToken(request, response);
    }

    /**
     * Metodo para alteracao de senha de um usuario
     *
     * @param novaSenhaDTO Objeto contendo a senha atual que deve ser alterada pela nova senha
     * @param bdResult     Interface geral para validação de dados.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Atualiza a senha de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha atualizada", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não localizado na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @PutMapping("/atualizar/alterar-senha")
    public ResponseEntity<?> atualizarSenha(
            @Parameter(description = "Senha do usuário a ser atualizada na base de dados") @Valid @RequestBody NovaSenhaDTO novaSenhaDTO,
            @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) {
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        usuarioService.atualizarSenha(novaSenhaDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Metodo para alteracao de senha de um usuario
     *
     * @param token Token de verificacao de registro do novo usuario gerado pela API e enviado no endereco eletronico
     *              cadastrado
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Valida registro de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro validado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não localizado na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @GetMapping(value = "/permitAll/validar-registro/{id}/{token}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> validarRegistro(
            @Parameter(description = "Chave identificadora(id) do usuário o qual a senha deve ser atualizada") @PathVariable(name = "id") Long id,
            @Parameter(description = "Token de verificacao de registro do novo usuário gerado pela API e enviado no endereco eletronico cadastrado") @PathVariable(name = "token") String token) throws MessagingException {
        String resultadoValidacao = usuarioService.validarRegistro(id, token);
        String linkMensagem = "http://localhost:3000/";
        String textoLink = "Clique aqui para voltar ao site!";

        if (resultadoValidacao.contains("sucesso")) {
            linkMensagem = "https://digital-booking.vercel.app/login";
            textoLink = "Clique aqui e faça login!";
        }

        String mensagemCorpo = "<style>\n" +
                "  .principal {\n" +
                "    display: flex;\n" +
                "    justify-content: center;\n" +
                "    align-items: center;\n" +
                "    width: 100vw;\n" +
                "    height: 100vh;\n" +
                "    background: #FFFFFF;\n" +
                "    color: #f0572d;\n" +
                "    font-family: 'Roboto';\n" +
                "    font-style: normal;\n" +
                "    font-weight: 500;\n" +
                "    font-size: 16px;\n" +
                "  }\n" +
                "\n" +
                "  a:hover {\n" +
                "    cursor: pointer;\n" +
                "  }\n" +
                "</style>\n" +
                "\n" +
                "\n" +
                "<main class=\"principal\">\n" +
                "  <p>" + resultadoValidacao + "</p><br>\n" +
                "  <div><a href=\"" + linkMensagem + "\">" + textoLink + "</a></div>\n" +
                "</main>";
        return ResponseEntity.status(HttpStatus.OK).body(mensagemCorpo);
    }

    /**
     * Metodo para buscar usuario via JWT
     *
     * @param request Requisicao do cliente e contem as informacoes ao seu respeito
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Busca um usuário via token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não localizado na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @GetMapping("/permitAll/me")
    public ResponseEntity<?> buscarUsuarioViaJWT(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarUsuarioViaJWT(request));
    }

    /**
     * Metodo para deletar um usuario.
     *
     * @param id Chave identificadora do usuario que deve ser deletado.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Deleta um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não localizado na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletar(
            @Parameter(description = "Chave identificadora(id) do usuário a ser deletado")
            @PathVariable(name = "id") Long id) {
        usuarioService.deletar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
