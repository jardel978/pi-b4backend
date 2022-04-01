package com.digitalbooking.projetointegrador.repository;

import com.digitalbooking.projetointegrador.model.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Classe repository para <strong>Imagem</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

public interface IImagemRepository  extends JpaRepository<Imagem, Long> {
}
