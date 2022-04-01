package com.digitalbooking.projetointegrador.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Classe para mapeamento da entidade Cliente
 *
 * @version 1.0
 * @since 1.0
 */

@Entity
@Getter
@Setter
@Table(name = "tb_cliente")
@NoArgsConstructor
@AllArgsConstructor
public class Cliente extends Usuario {



    @JsonIgnore
    @OneToMany(mappedBy = "cliente", orphanRemoval = true)
    private Set<Reserva> reservas;

    private String endereco;

}
