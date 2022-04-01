package com.digitalbooking.projetointegrador.repository;

import com.digitalbooking.projetointegrador.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Classe repository para <strong>Produto</strong>.
 *
 * @version 1.0
 * @since 1.0
 */
public interface IProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findAllByCategoriaNomeContainsIgnoreCase(String nomeCategoria);

    List<Produto> findAllByCidadeNomeContainsIgnoreCase(String nomeCidade);
}
