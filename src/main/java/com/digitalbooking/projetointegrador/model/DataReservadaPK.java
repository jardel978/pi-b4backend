package com.digitalbooking.projetointegrador.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable//indica que essa classe é chave composta
public class DataReservadaPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

}
