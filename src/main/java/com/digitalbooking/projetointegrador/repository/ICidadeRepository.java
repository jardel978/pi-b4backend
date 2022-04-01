package com.digitalbooking.projetointegrador.repository;

import com.digitalbooking.projetointegrador.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Classe repository para <strong>Cidade</strong>.
 *
 * @version 1.0
 * @since 1.0
 */
public interface ICidadeRepository extends JpaRepository<Cidade, Long> {
}
