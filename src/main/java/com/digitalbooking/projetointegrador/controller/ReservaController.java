package com.digitalbooking.projetointegrador.controller;


import com.digitalbooking.projetointegrador.controller.exception.CampoInvalidoException;
import com.digitalbooking.projetointegrador.dto.ReservaDTO;
import com.digitalbooking.projetointegrador.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Classe de controller para <strong>Reserva</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@RestController()
@RequestMapping("/reservas")
//SpringDoc documentação
@Tag(name = "Reservas", description = "API REST Reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    /**
     * Metodo para salvar uma reserva.
     *
     * @param reservaDTO Reserva que deve ser persistida no banco de dados.
     * @return Response HTTP personalizada com HttpStatus 201.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Salva uma reserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva salva", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @PostMapping("/cliente/salvar")
    public ResponseEntity<?> salvar(@Parameter(description = "Reserva a ser salva na base de dados") @Valid @RequestBody ReservaDTO reservaDTO,
                                    @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) {
        reservaService.salvar(reservaDTO);
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Metodo que faz a busca de todas as reservas.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Response HTTP personalizada contendo HttpStatus 200 e um objeto de paginacao em seu corpo.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Retorna todas as reservas paginadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas encontradas"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção", content = @Content(schema =
            @Schema(implementation = HandlerError.class))),
    })
    @GetMapping
    public ResponseEntity<Page<ReservaDTO>> buscarTodos(@ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(reservaService.buscarTodos(pageable));
    }

    /**
     * Metodo que faz a busca de todas as reservas.
     *
     * @param emailCliente Email do cliente o qual queremos retormar todas as suas reservas.
     * @return Response HTTP personalizada contendo HttpStatus 200 e uma lista de reservas em seu corpo.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Retorna uma lista com todas as reservas de um determinado cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas encontradas"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção", content = @Content(schema =
            @Schema(implementation = HandlerError.class))),
    })
    @GetMapping("/cliente/buscar")
    public ResponseEntity<List<ReservaDTO>> buscarTodosDeUmCliente(
            @Parameter(description = "Email do cliente o qual queremos retormar todas as suas reservas")
            @RequestParam(value = "email-cliente") String emailCliente) {
        return ResponseEntity.status(HttpStatus.OK).body(reservaService.buscarTodosDeUmCliente(emailCliente));
    }

    /**
     * Metodo responsavel por atualizar uma reserva.
     *
     * @param reservaDTO Reserva que deve ser atualizada.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Atualiza uma reserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva atualizada", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Reserva não localizada na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @PutMapping("/cliente/atualizar")
    public ResponseEntity<?> atualizar(@Parameter(description = "Reserva a ser atualizada na base de dados") @Valid @RequestBody ReservaDTO reservaDTO,
                                       @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) {
        reservaService.atualizar(reservaDTO);
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Metodo para deletar uma reserva.
     *
     * @param id Chave identificadora da reserva que deve ser deletada.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Deleta uma reserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva deletada", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Reserva não localizado na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @DeleteMapping("/cliente/deletar/{id}")
    public ResponseEntity<?> deletar(
            @Parameter(description = "Chave identificadora(id) da Reserva a ser deletada")
            @PathVariable(name = "id") Long id) {
        reservaService.deletar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
