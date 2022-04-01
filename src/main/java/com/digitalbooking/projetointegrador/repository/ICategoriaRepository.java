package com.digitalbooking.projetointegrador.repository;

import com.digitalbooking.projetointegrador.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Classe repository para <strong>Categoria</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {



}
