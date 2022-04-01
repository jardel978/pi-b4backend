package com.digitalbooking.projetointegrador.controller;

import com.digitalbooking.projetointegrador.controller.exception.CampoInvalidoException;
import com.digitalbooking.projetointegrador.dto.CategoriaDTO;
import com.digitalbooking.projetointegrador.service.CategoriaService;
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
 * Classe de controller para <strong>Categoria</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@RestController()
@RequestMapping("/categorias")
//SpringDoc documentação
@Tag(name = "Categorias", description = "API REST Categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    /**
     * Metodo para salvar uma categoria.
     *
     * @param categoriaDTO Categoria que deve ser persistida no banco de dados.
     * @return Response HTTP personalizada com HttpStatus 201.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Salva uma categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria salva", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(@Valid @RequestBody CategoriaDTO categoriaDTO, BindingResult bdResult) {
        categoriaService.salvar(categoriaDTO);
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Metodo que faz a busca de todas as categorias.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Response HTTP personalizada contendo HttpStatus 200 e um objeto de paginacao em seu corpo.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Retorna todas as categorias paginadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorias encontradas"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção", content = @Content(schema =
            @Schema(implementation = HandlerError.class))),
    })
    @GetMapping("/permitAll")
    public ResponseEntity<Page<CategoriaDTO>> buscarTodos(@ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.buscarTodos(pageable));
    }

    /**
     * Metodo responsavel por atualizar uma categoria.
     *
     * @param categoriaDTO Categoria que deve ser atualizada.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Atualiza uma categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada", content = @Content),
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
    public ResponseEntity<?> atualizar(@Valid @RequestBody CategoriaDTO categoriaDTO, BindingResult bdResult) {
        categoriaService.atualizar(categoriaDTO);
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Metodo para deletar uma categoria.
     *
     * @param id Chave identificadora da categoria que deve ser deletada.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Deleta uma categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria deletada", content = @Content),
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
            @Parameter(description = "Chave identificadora(id) da categoria a ser deletada")
            @PathVariable(name = "id") Long id) {
        categoriaService.deletar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
