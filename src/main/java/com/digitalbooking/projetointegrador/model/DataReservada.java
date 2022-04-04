package com.digitalbooking.projetointegrador.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "tb_data_reserva_qtd")
@NoArgsConstructor
@AllArgsConstructor
public class DataReservada implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private DataReservadaPK id = new DataReservadaPK();

    @Column(name = "quantidade_reservas")
    private int qdtPessoasReservadas;

}
