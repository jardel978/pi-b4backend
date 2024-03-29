package com.digitalbooking.projetointegrador.dto;

import com.digitalbooking.projetointegrador.model.Reserva;
import com.digitalbooking.projetointegrador.model.enums.Moeda;
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

    private static final long serialVersionUID = 1L;

    private Long id;

    private String transacaoId;

    @NotNull(message = "O valor é um campo obrigatário")
    private int valor;

    @NotNull(message = "O token é um campo obrigatário")
    private String tokenId;

    private String descricao;

    @NotNull(message = "A moeda deve ser informada")
    private Moeda moeda;

    private Reserva reserva;

}
