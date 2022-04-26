package com.digitalbooking.projetointegrador.repository;

import com.digitalbooking.projetointegrador.model.DataReservada;
import com.digitalbooking.projetointegrador.model.DataReservadaPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe repository para <strong>DataReservada</strong>.
 *
 * @version 1.0
 * @since 1.0
 */
public interface IDataReservadaRepository extends JpaRepository<DataReservada, DataReservadaPK> {

    List<DataReservada> findAllByIdData(LocalDate data);
    List<DataReservada> findAllByIdProdutoId(Long idProduto);

}
