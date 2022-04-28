package com.digitalbooking.projetointegrador.repository;


import com.digitalbooking.projetointegrador.model.OrdemDePagamento;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Classe repository para <strong>OrdemDePagemento</strong>.
 *
 * @version 1.0
 * @since 1.0
 */
public interface IOrdemPagamentoRepository extends JpaRepository<OrdemDePagamento, Long> {

}
