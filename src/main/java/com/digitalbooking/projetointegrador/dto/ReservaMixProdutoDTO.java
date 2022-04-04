package com.digitalbooking.projetointegrador.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaMixProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private LocalDate dataInicio;

    private LocalDate dataFinal;

    private UsuarioMixReservaDTO usuario;

}
