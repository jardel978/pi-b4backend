package com.digitalbooking.projetointegrador.controller;


import com.digitalbooking.projetointegrador.controller.exception.CampoInvalidoException;
import com.digitalbooking.projetointegrador.dto.ClienteDTO;
import com.digitalbooking.projetointegrador.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Classe de controller para <strong>Cliente</strong>.
 *
 * @version 1.0
 * @since 1.0
 */


@RestController
@RequestMapping("/clientes")
//SpringDoc documentação
@Tag(name = "Clientes", description = "API REST Clientes")
public class ClienteController extends UsuarioController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Metodo para salvar um cliente.
     *
     * @param clienteDTO Usuario que deve ser persistido no banco de dados.
     * @param bdResult   Interface geral para validação de dados.
     * @return Response HTTP personalizada com HttpStatus 201.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Salva um cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente salvo", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @PostMapping("/permitAll/salvar")
    public ResponseEntity<?> salvar(@Parameter(description = "Cliente a ser salvo na base de dados") @Valid @RequestBody ClienteDTO clienteDTO,
                                    @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) {
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        clienteService.salvar(clienteDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Metodo para busca de cliente por id.
     *
     * @param id Chave identificadora do cliente a ser buscado
     * @return Cliente encontrado
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Busca um cliente pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção", content = @Content(schema =
            @Schema(implementation = HandlerError.class))),
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "Chave identificadora(id) do cliente a ser buscado") @PathVariable(name = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.buscarPorId(id));
    }

    /**
     * Metodo responsavel por atualizar um cliente.
     *
     * @param clienteDTO Cliente que deve ser atualizado.
     * @param bdResult   Interface geral para validação de dados.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Atualiza um cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não localizado na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @PutMapping("/atualizar/cliente")
    public ResponseEntity<?> atualizar(@Parameter(description = "Cliente a ser atualizado na base de dados") @Valid @RequestBody ClienteDTO clienteDTO,
                                       @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) {
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        clienteService.atualizar(clienteDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
