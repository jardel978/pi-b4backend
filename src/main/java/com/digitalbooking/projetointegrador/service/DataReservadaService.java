package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.model.DataReservada;
import com.digitalbooking.projetointegrador.repository.IDataReservadaRepository;
import com.digitalbooking.projetointegrador.utils.DatasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe de service para <strong>DataReservada</strong>.
 *
 * @version 1.0
 * @since 1.0
 */
@Service
@EnableScheduling
public class DataReservadaService {

    private static final long RECORRENCIA_PARA_EXCLUIR_REGISTROS_DATA_RESERVADA = 1000L * 60 * 60 * 24 * 30;//30 dias

    @Autowired
    private IDataReservadaRepository dataReservadaRepository;

    @Autowired
    private DatasUtil datasUtil;

    @Async
    @Transactional
    @Scheduled(fixedDelay = RECORRENCIA_PARA_EXCLUIR_REGISTROS_DATA_RESERVADA)
//executa esse método uma vez a cada intervalo de tempo determinado
    public void removerDatasReservadasPassadas() {//remove os registros do tipo DatasReservadas antigos (anteriores a data atual)
        LocalDate dataDeInicioDaTarefa = LocalDate.now();

        List<LocalDate> listaDeDatasUmMesAnteriorDaDataInicial =
                datasUtil.gerarListaDeDatasUmMesAnteriorDaDataInicial(dataDeInicioDaTarefa);
        listaDeDatasUmMesAnteriorDaDataInicial.forEach(data -> {
            List<DataReservada> datasReservadas = dataReservadaRepository.findAllByIdData(data);//buscar todas as datas já passadas
            if (!datasReservadas.isEmpty()) {
                datasReservadas.forEach(dataParaExcluir -> dataReservadaRepository.deleteById(dataParaExcluir.getId()));
            }
        });
    }
}
