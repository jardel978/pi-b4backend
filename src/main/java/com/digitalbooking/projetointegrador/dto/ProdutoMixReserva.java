package com.digitalbooking.projetointegrador.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoMixReserva implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String nome;

    private CidadeDTO cidade;

    private CategoriaDTO categoria;

    private Set<ImagemDTO> imagens;

}
