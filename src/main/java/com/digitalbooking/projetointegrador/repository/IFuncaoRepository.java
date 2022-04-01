package com.digitalbooking.projetointegrador.repository;


import com.digitalbooking.projetointegrador.model.Funcao;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Classe repository para <strong>Funcao</strong>.
 *
 * @version 1.0
 * @since 1.0
 */
public interface IFuncaoRepository extends JpaRepository<Funcao, Long> {
}
