package com.digitalbooking.projetointegrador.dto;

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
public class ReservaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "O horário inicial da reserva não pode ser nulo")
    private LocalTime horarioInicio;

    @NotNull(message = "A data inicial da reserva não pode ser nula")
    private LocalDate dataInicio;

    @NotNull(message = "A data final da reserva não pode ser nula")
    private LocalDate dataFinal;

    @NotNull(message = "O produto da reserva deve ser informado")
    private ProdutoMixReserva produto;

    @NotNull(message = "O usuario da reserva deve ser informado")
    private UsuarioMixReservaDTO usuario;

    @NotNull(message = "A quantidade de pessoas deve ser informada")
    private Integer qtdPessoas;

    private Double valorTotal;

    private StatusReserva status;

    private OrdemDePagamentoMixReservaDTO ordemDePagamento;

}
