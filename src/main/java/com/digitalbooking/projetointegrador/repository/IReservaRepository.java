package com.digitalbooking.projetointegrador.repository;

import com.digitalbooking.projetointegrador.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Classe repository para <strong>Reserva</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

public interface IReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findAllByClienteEmail(String emailCliente);

}
