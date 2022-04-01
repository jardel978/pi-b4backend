package com.digitalbooking.projetointegrador.controller;

import com.digitalbooking.projetointegrador.controller.exception.CampoInvalidoException;
import com.digitalbooking.projetointegrador.dto.FuncaoDTO;
import com.digitalbooking.projetointegrador.service.FuncaoService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * Classe de controller para <strong>Funcao</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@RestController()
@RequestMapping("/funcoes")
//SpringDoc documentação
@Tag(name = "Funcoes", description = "API REST Funcoes")
public class FuncaoController {

    @Autowired
    private FuncaoService funcaoService;

    // CREATE

    /**
     * Metodo para salvar uma funcao.
     *
     * @param funcaoDTO Funcao que deve ser persistida no banco de dados.
     * @return Response HTTP personalizada com HttpStatus 201.
     * @since 1.0
     */

    // SpringDoc documentação
    @Operation(summary = "Salva uma funcao")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funcao salva", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(@Parameter(description = "Função a ser salva na base de dados") @Valid @RequestBody FuncaoDTO funcaoDTO,
                                    @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) {
        funcaoService.salvar((funcaoDTO));
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // READ

    /**
     * Metodo que faz a busca de todas as funçoes.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Response HTTP personalizada contendo HttpStatus 200 e um objeto de paginacao em seu corpo.
     * @since 1.0
     */

    //SpringDoc documentação
    @Operation(summary = "Retorna todas as funções paginadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funções encontradas"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção", content = @Content(schema =
            @Schema(implementation = HandlerError.class))),
    })
    @GetMapping()
    public ResponseEntity<Page<FuncaoDTO>> buscarTodos(@ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(funcaoService.buscarTodos(pageable));
    }

    // PUT

    /**
     * Metodo responsavel por atualizar uma função.
     *
     * @param funcaoDTO Função que deve ser atualizada.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Atualiza uma função")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Função atualizada", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Função não localizada na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizar(@Parameter(description = "Função a ser atualizada na base de dados") @Valid @RequestBody FuncaoDTO funcaoDTO,
                                       @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) {
        funcaoService.atualizar(funcaoDTO);
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        return new ResponseEntity<>(HttpStatus.OK);
    }


    // DELETE

    /**
     * Metodo para deletar uma funcao.
     *
     * @param id Chave identificadora da cidade que deve ser deletada.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */

    //SpringDoc documentação
    @Operation(summary = "Deleta uma função")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Função deletada", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Função não localizada na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })

    @Transactional
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletar(
            @Parameter(description = "Chave identificadora(id) da função a ser deletada")
            @PathVariable(name = "id") Long id) {
        funcaoService.deletar(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
