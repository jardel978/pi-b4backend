package com.digitalbooking.projetointegrador.model;


import lombok.*;

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
@Table(name = "tb_imagem")
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "url"})
@NoArgsConstructor
public class Imagem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "imagem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String url;

    @ManyToOne
    @JoinColumn(name = "produto_id", foreignKey = @ForeignKey(name = "fk_produto_imagem"))
    private Produto produto;

    @Column(name = "img_capa")
    private boolean ehImagemCapa;

}
