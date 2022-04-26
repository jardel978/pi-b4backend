package com.digitalbooking.projetointegrador.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Classe para criacao de ordens de pagamentos
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdemDePagamentoDTO implements Serializable {

    public enum Moeda {
        BRL, EUR, USD;
    }

    private static final long serialVersionUID = 1L;

    @NotNull(message = "O valor é um campo obrigatário")
    private int valor;

    @NotNull(message = "O token é um campo obrigatário")
    private String tokenId;

    private String descricao;

    @NotNull(message = "A moeda deve ser informada")
    private Moeda moeda;


}
