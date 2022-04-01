package com.digitalbooking.projetointegrador.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe mapeada para a criacao da entidade Cidade.
 *
 * @version 1.0
 * @since 1.0
 */

@Entity
@Getter
@Setter
@Table(name = "tb_cidade")
@AllArgsConstructor
@NoArgsConstructor
public class Cidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "cidade_tb")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    private String pais;

    @JsonIgnore
    @OneToMany(mappedBy = "cidade")
    private Set<Produto> produtos = new HashSet<>();

}
