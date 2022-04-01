package com.digitalbooking.projetointegrador.repository;

import com.digitalbooking.projetointegrador.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Classe repository para <strong>Usuario</strong>.
 *
 * @version 1.0
 * @since 1.0
 */
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

}
