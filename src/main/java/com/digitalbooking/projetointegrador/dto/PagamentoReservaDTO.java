package com.digitalbooking.projetointegrador.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Classe para transporte de dados (DTO) entre as camadas de service, controller e entre cliente e servidor.
 *
 * @version 1.0
 * @since 1.0
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoReservaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "O id da reserva é um campo obrigatário")
    private Long reservaId;

    @NotNull(message = "O id da ordem de pagamento é um campo obrigatário")
    private Long ordemDePagamentoId;

}
