package com.digitalbooking.projetointegrador.controller;


import com.digitalbooking.projetointegrador.controller.exception.CampoInvalidoException;
import com.digitalbooking.projetointegrador.dto.CaracteristicaDTO;
import com.digitalbooking.projetointegrador.service.CaracteristicaService;
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

/**
 * Classe de controller para <strong>Caracteristica</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@RestController()
@RequestMapping("/caracteristicas")
//SpringDoc documentação
@Tag(name = "Caracteristicas", description = "API REST Caracteristicas")
public class CaracteristicaController {

    @Autowired
    private CaracteristicaService caracteristicaService;

    /**
     * Metodo para salvar uma caracteristica.
     *
     * @param caracteristicaDTO Caracteristica que deve ser persistida no banco de dados.
     * @return Response HTTP personalizada com HttpStatus 201.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Salva uma caracteristica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cidade salva", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(@Parameter(description = "Característica a ser salva na base de dados") @Valid @RequestBody CaracteristicaDTO caracteristicaDTO,
                                    @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) {
        caracteristicaService.salvar(caracteristicaDTO);
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Metodo que faz a busca de todas as caracteristicas.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Response HTTP personalizada contendo HttpStatus 200 e um objeto de paginacao em seu corpo.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Retorna todas as caracteristicas paginadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caracteristicas encontradas"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção", content = @Content(schema =
            @Schema(implementation = HandlerError.class))),
    })
    @GetMapping()
    public ResponseEntity<Page<CaracteristicaDTO>> buscarTodos(@ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(caracteristicaService.buscarTodos(pageable));
    }

    /**
     * Metodo responsavel por atualizar uma caracteristica.
     *
     * @param caracteristicaDTO Caracteristica que deve ser atualizada.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Atualiza uma caracteristica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caracteristica atualizada", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Produto não localizado na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizar(@Parameter(description = "Característica a ser atualizada na base de dados") @Valid @RequestBody CaracteristicaDTO caracteristicaDTO,
                                       @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) {
        caracteristicaService.atualizar(caracteristicaDTO);
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Metodo para deletar uma caracteristica.
     *
     * @param id Chave identificadora da caracteristica que deve ser deletada.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Deleta uma caracteristica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caracteristica deletada", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Produto não localizado na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletar(
            @Parameter(description = "Chave identificadora(id) da caracteristica a ser deletada")
            @PathVariable(name = "id") Long id) {
        caracteristicaService.deletar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
