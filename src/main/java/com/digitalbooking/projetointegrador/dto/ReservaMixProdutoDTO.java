package com.digitalbooking.projetointegrador.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class ReservaMixProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private LocalDate dataInicio;

    private LocalDate dataFinal;

    private UsuarioMixReservaDTO usuario;

}
