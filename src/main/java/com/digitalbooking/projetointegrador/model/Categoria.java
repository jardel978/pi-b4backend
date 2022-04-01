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
 * Classe mapeada para a criacao da entidade Categoria.
 *
 * @version 1.0
 * @since 1.0
 */

@Entity
@Getter
@Setter
@Table(name = "tb_categoria")
@AllArgsConstructor
@NoArgsConstructor
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "categoria_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    private String qualificacao;
    private String descricao;
    private String urlImagem;

    @JsonIgnore
    @OneToMany(mappedBy = "categoria")
    private Set<Produto> produtos = new HashSet<>();

}
