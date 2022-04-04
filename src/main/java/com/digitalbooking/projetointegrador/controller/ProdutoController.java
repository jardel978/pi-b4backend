package com.digitalbooking.projetointegrador.controller;

import com.digitalbooking.projetointegrador.controller.exception.CampoInvalidoException;
import com.digitalbooking.projetointegrador.dto.ProdutoDTO;
import com.digitalbooking.projetointegrador.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

/**
 * Classe de controller para <strong>Produto</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@RestController
@RequestMapping("/produtos")
//SpringDoc documentação
@Tag(name = "Produtos", description = "API REST Produtos")
@SecurityRequirement(name = "apidigitalbooking")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    /**
     * Metodo para salvar um produto.
     *
     * @param produtoDTO Produto que deve ser persistido no banco de dados.
     * @param bdResult   Interface geral para validação de dados.
     * @return Response HTTP personalizada com HttpStatus 201.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Salva um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto salvo", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @Transactional
    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(@Parameter(description = "Produto a ser salvo na base de dados") @Valid @RequestBody ProdutoDTO produtoDTO,
                                    @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) {
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        produtoService.salvar(produtoDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Metodo que busca um produto por id.
     *
     * @param id Chave identificadora do produto que deve ser buscado.
     * @return Produto buscado.
     */
    //SpringDoc documentação
    @Operation(summary = "Busca um produto pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado", content = @Content(schema =
            @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Produto não localizado na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @GetMapping("/permitAll/buscar/{id}")
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "Id do produto a ser buscado") @PathVariable(name = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscarPorId(id));
    }

    /**
     * Metodo que permite filtrar produtos por cidade e duas datas.
     *
     * @param nomeCidade  Nome da cidade a ser buscada.
     * @param dataInicial Data inicial a ser buscada.
     * @param dataFinal   Data final a ser buscada.
     * @return Produtos disponíveis encontrados.
     */
    //SpringDoc documentação
    @Operation(summary = "Busca todos os produto que estão disponíveis para uma cidade e em um determinado intervalo " +
            "de datas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados", content = @Content(schema =
            @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "404", description = "Produto não localizado na base de dados",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @GetMapping("/permitAll/buscar")
    public ResponseEntity<?> buscarPorCidadeDatas(@Parameter(description = "Nome da cidade em que o produto se localiza") @RequestParam(name = "cidade") String nomeCidade,
                                                  @Parameter(description = "Data inicial do período") @RequestParam(name = "data-inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                  @Parameter(description = "Data final do período") @RequestParam(name = "data-fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
                                                  @Parameter(description = "Quantidade de pessoas almejada para a " +
                                                          "busca") @RequestParam(name = "num-pessoas") Integer qtdPessoasPretendidas) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscarPorCidadeDatas(nomeCidade, dataInicial,
                dataFinal, qtdPessoasPretendidas));
    }

    /**
     * Metodo para buscar todos os produtos por sua categoria.
     *
     * @param nomeCategoria Nome da categoria a ser buscada.
     * @return Lista com todos os produtos de uma determinada categoria.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Busca produtos por categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados", content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = ProdutoDTO.class))
            )),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção", content = @Content),
    })
    @GetMapping("/permitAll/buscar/categorias")
    public ResponseEntity<?> buscarPorCategoria(
            @Parameter(description = "Nome da categoria a qual queremos buscar os produtos associados")
            @RequestParam(value = "nome-categoria") String nomeCategoria) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscarPorCategoria(nomeCategoria));
    }

    /**
     * Metodo para buscar todos os produtos associados a uma cidade.
     *
     * @param nomeCidade Nome da cidade a ser buscada.
     * @return Lista com todos os produtos de uma determinada cidade.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Busca produtos por cidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados", content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = ProdutoDTO.class))
            )),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção", content = @Content),
    })
    @GetMapping("/permitAll/buscar/cidades")
    public ResponseEntity<?> buscarPorCidade(
            @Parameter(description = "Nome da cidade a qual queremos buscar os produtos associados")
            @RequestParam(value = "nome-cidade") String nomeCidade) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscarPorCidade(nomeCidade));
    }

    /**
     * Metodo que faz a busca de todos os produtos.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Response HTTP personalizada contendo HttpStatus 200 e um objeto de paginacao em seu corpo.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Retorna todos os produtos paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção", content = @Content(schema =
            @Schema(implementation = HandlerError.class))),
    })
    @GetMapping("/permitAll")
    public ResponseEntity<Page<ProdutoDTO>> buscarTodos(@ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscarTodos(pageable));
    }

    /**
     * Metodo responsavel por atualizar um produto.
     *
     * @param produtoDTO Produto que deve ser atualizado.
     * @param bdResult   Interface geral para validação de dados.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Atualiza um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado", content = @Content),
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
    public ResponseEntity<?> atualizar(@Parameter(description = "Produto a ser atualizado na base de dados") @Valid @RequestBody ProdutoDTO produtoDTO,
                                       @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) {
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        produtoService.atualizar(produtoDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Metodo para deletar um produto.
     *
     * @param id Chave identificadora do produto que deve ser deletado.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Deleta um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto deletado", content = @Content),
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
            @Parameter(description = "Chave identificadora(id) do produto a ser deletado")
            @PathVariable(name = "id") Long id) {
        produtoService.deletar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
