package com.digitalbooking.projetointegrador.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Classe para mapeamento da entidade Reserva
 *
 * @version 1.0
 * @since 1.0
 */

@Entity
@Getter
@Setter
@Table(name = "tb_reserva")
@NoArgsConstructor
@AllArgsConstructor
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "reserva_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //Horário de início da reserva
    private LocalTime horarioInicio;


    //Data inicial da reserva
    private LocalDate dataInicio;


    //Data final da reserva.
    private LocalDate dataFinal;


    //Criar relacionamento Muitos para Um com a tabela "produtos"
    @ManyToOne
    @JoinColumn(name = "produto_id", foreignKey = @ForeignKey(name = "fk_produto_reserva"))
    private Produto produto;

    //Criar relacionamento Muitos para Um com a tabela "usuários"
    @ManyToOne
    @JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "fk_usuario_reserva"))
    private Cliente cliente;

    @Column(name = "qtd_pessoas")
    private Integer qtdPessoas;

}
