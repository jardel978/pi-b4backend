package com.digitalbooking.projetointegrador.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idProduto;
    private Long idCliente;
    private Double valor;

}
