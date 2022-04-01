package com.digitalbooking.projetointegrador.controller;

import com.digitalbooking.projetointegrador.controller.exception.CampoInvalidoException;
import com.digitalbooking.projetointegrador.dto.ImagemDTO;
import com.digitalbooking.projetointegrador.service.ImagemService;
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
 * Classe de controller para <strong>Imagem</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@RestController()
@RequestMapping("/imagens")
//SpringDoc documentação
@Tag(name = "Imagens", description = "API REST Imagens")
public class ImagemController {

    @Autowired
    private ImagemService imagemService;

    /**
     * Metodo para salvar uma imagem.
     *
     * @param imagemDTO Imagem que deve ser persistida no banco de dados.
     * @return Response HTTP personalizada com HttpStatus 201.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Salva uma imagem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Imagem salva", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(@Parameter(description = "Imagem a ser salva na base de dados") @Valid @RequestBody ImagemDTO imagemDTO,
                                    @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) {
        imagemService.salvar(imagemDTO);
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Metodo que faz a busca de todas as imagens.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Response HTTP personalizada contendo HttpStatus 200 e um objeto de paginacao em seu corpo.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Retorna todas as imagens paginadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagens encontradas"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção", content = @Content(schema =
            @Schema(implementation = HandlerError.class))),
    })
    @GetMapping()
    public ResponseEntity<Page<ImagemDTO>> buscarTodos(@ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(imagemService.buscarTodos(pageable));
    }

    /**
     * Metodo responsavel por atualizar uma imagem.
     *
     * @param imagemDTO Imagem que deve ser atualizada.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Atualiza uma imagem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem atualizada", content = @Content),
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
    public ResponseEntity<?> atualizar(@Parameter(description = "Imagem a ser atualizada na base de dados") @Valid @RequestBody ImagemDTO imagemDTO,
                                       @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) {
        imagemService.atualizar(imagemDTO);
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Metodo para deletar uma imagem.
     *
     * @param id Chave identificadora da imagem que deve ser deletada.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Deleta uma imagem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem deletada", content = @Content),
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
            @Parameter(description = "Chave identificadora(id) da imagem a ser deletada")
            @PathVariable(name = "id") Long id) {
        imagemService.deletar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
