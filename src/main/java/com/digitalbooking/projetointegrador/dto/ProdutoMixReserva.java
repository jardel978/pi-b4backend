package com.digitalbooking.projetointegrador.dto;

import java.io.Serializable;

public class ProdutoMixReserva implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String nome;

    private CidadeDTO cidade;

    private CategoriaDTO categoria;

}
