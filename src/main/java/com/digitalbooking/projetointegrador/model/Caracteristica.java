package com.digitalbooking.projetointegrador.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * Classe mapeada para a criacao da entidade Caracteristica.
 *
 * @version 1.0
 * @since 1.0
 */

@Entity
@Getter
@Setter
@Table(name = "tb_caracteristica")
@AllArgsConstructor
@NoArgsConstructor
public class Caracteristica implements Serializable {

    //SerialVersionUID é um número que identifica a versão da classe que foi usada durante o processo de serialização
    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "caracteristica_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    private String icone;

    @ManyToMany(mappedBy = "caracteristicas")
    private Set<Produto> produtos = new HashSet<>();

}
