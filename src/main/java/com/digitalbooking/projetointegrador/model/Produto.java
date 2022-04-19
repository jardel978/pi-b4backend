package com.digitalbooking.projetointegrador.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe para mapeamento da entidade Produto
 *
 * @version 1.0
 * @since 1.0
 */

@Entity
@Getter
@Setter
@Table(name = "tb_produto")
@NoArgsConstructor
@AllArgsConstructor
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

      @Id
    @Column(name = "produto_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    @Column(length = 3000)
    private String descricao;

    @Column(name = "nota_avaliacao_clientes")
    private Double notaDeAvaliacao;

    @Column(name = "valor_por_pessoa")
    private Double valorPorPessoa;

    @Column(name = "qtd_avaliacoes")
    private int qtdAvaliacoes;

    @JsonIgnore
    @OneToMany(mappedBy = "produto", orphanRemoval = true)
    private Set<Imagem> imagens = new HashSet<>();

    @Column(unique = true)
    private String endereco;

    @ManyToOne
    @JoinColumn(name = "cidade_id", foreignKey = @ForeignKey(name = "fk_cidade_produto"))
    private Cidade cidade;

    private Double latitude;

    private Double longitude;

    @ManyToOne//muitos produtos para uma categoria
    @JoinColumn(name = "categoria_id", foreignKey = @ForeignKey(name = "fk_categoria_produto"))
    private Categoria categoria;

    @OneToMany(mappedBy = "id.produto")
    private Set<Score> scores = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "tb_caracteristica_produto",
            joinColumns = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "caracteristica_id")
    )
    private Set<Caracteristica> caracteristicas = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "produto", orphanRemoval = true)
    private Set<Reserva> reservas;

    @Column(name = "limite_pessoas_por_dia")
    private Integer limitePessoasPorDia;

    @Transient//indica que esse campo não será persistido
    private List<LocalDate> datasIndisponiveis = new ArrayList<>();


}
