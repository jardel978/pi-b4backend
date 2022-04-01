package com.digitalbooking.projetointegrador.repository;


import com.digitalbooking.projetointegrador.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Classe repository para <strong>Cliente</strong>.
 *
 * @version 1.0
 * @since 1.0
 */
public interface IClienteRepository extends JpaRepository<Cliente, Long> {


}
