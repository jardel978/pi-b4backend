package com.digitalbooking.projetointegrador.model;


import com.digitalbooking.projetointegrador.model.enums.NomeFuncao;
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
 * Classe mapeada para a criacao da entidade Funcao.
 *
 * @version 1.0
 * @since 1.0
 */

@Entity
@Getter
@Setter
@Table(name = "tb_funcao")
@AllArgsConstructor
@NoArgsConstructor
public class Funcao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "funcao_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private NomeFuncao nomeFuncaoEnum;

    @OneToMany(mappedBy = "funcao")
    @JsonIgnore
    private Set<Usuario> usuarios = new HashSet<>();

}
