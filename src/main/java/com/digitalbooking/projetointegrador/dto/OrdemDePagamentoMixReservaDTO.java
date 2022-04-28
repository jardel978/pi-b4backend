package com.digitalbooking.projetointegrador.dto;

import com.digitalbooking.projetointegrador.model.enums.Moeda;
import com.digitalbooking.projetointegrador.model.enums.StatusReserva;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

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
public class OrdemDePagamentoMixReservaDTO implements Serializable {

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

}
