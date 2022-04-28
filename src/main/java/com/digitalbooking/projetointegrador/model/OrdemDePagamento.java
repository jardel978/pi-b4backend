package com.digitalbooking.projetointegrador.model;


import com.digitalbooking.projetointegrador.model.enums.Moeda;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Classe mapeada para a criacao da entidade Imagem.
 *
 * @version 1.0
 * @since 1.0
 */


@Entity
@Getter
@Setter
@Table(name = "tb_ordem_pagamento")
@AllArgsConstructor
@NoArgsConstructor
public class OrdemDePagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ordem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transacaoId;

    private int valor;

    private String tokenId;

    private String descricao;

    private Moeda moeda;

    @OneToOne
    private Reserva reserva;

}
