package com.digitalbooking.projetointegrador.controller;

import com.digitalbooking.projetointegrador.controller.exception.CampoInvalidoException;
import com.digitalbooking.projetointegrador.dto.OrdemDePagamentoDTO;
import com.digitalbooking.projetointegrador.service.PagamentoService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Classe de controller para criacao de ordens de Pagamentos.
 *
 * @version 1.0
 * @since 1.0
 */

@RestController
@RequestMapping("/pagamentos")
@Tag(name = "Pagamentos", description = "API REST Pagamentos")
@SecurityRequirement(name = "apidigitalbooking")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    /**
     * Metodo para salvar um cliente.
     *
     * @param ordemDTO Objeto contendo informações para geração da ordem de pagamento.
     * @param bdResult Interface geral para validação de dados.
     * @return Response HTTP personalizada com HttpStatus 200.
     * @since 1.0
     */
    //SpringDoc documentação
    @Operation(summary = "Retorna uma string criada para identificar e soilicitar uma ordem de pagamento para o " +
            "Stripe (Api de pagamentos)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordem criada", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção",
                    content = @Content(schema = @Schema(implementation = HandlerError.class))),
    })
    @PostMapping
    public ResponseEntity<?> carregarOrdemDePagamento(
            @Parameter(description = "Dados para gerar a ordem de pagamento") @RequestBody @Valid OrdemDePagamentoDTO ordemDTO,
            @Parameter(description = "Interface geral para validação de dados recebidos") BindingResult bdResult) throws StripeException {
        if (bdResult.hasErrors())
            throw new CampoInvalidoException(bdResult.getAllErrors().get(0).getDefaultMessage());

        return ResponseEntity.status(HttpStatus.OK).body(pagamentoService.carregarOrdemDePagamento(ordemDTO));
    }

}
