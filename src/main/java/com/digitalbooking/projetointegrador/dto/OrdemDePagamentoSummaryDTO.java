package com.digitalbooking.projetointegrador.dto;

import com.digitalbooking.projetointegrador.model.Reserva;
import com.digitalbooking.projetointegrador.model.enums.Moeda;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Classe para retorno de ordens de pagamentos criadas
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdemDePagamentoSummaryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String transacaoId;

}
