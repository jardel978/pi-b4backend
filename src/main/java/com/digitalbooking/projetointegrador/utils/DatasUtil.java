package com.digitalbooking.projetointegrador.utils;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class DatasUtil {

    /**
     * Método que retorna uma lista de datas dentro de um período entre datas
     *
     * @param dataInicial Data inicial do período
     * @param dataFinal   Data final do período
     * @return Lista contendo todas as datas iniciando da dataInical(inclusivo) até a dataFinal(inclusivo)
     */
    public List<LocalDate> gerarPeriodoDeDatas(LocalDate dataInicial, LocalDate dataFinal) {
        List<LocalDate> datas = new ArrayList<>();
        long dias = dataInicial.until(dataFinal, ChronoUnit.DAYS);
        for (int i = 0; i <= dias; i++) {
            LocalDate data = dataInicial.plusDays(i);
            datas.add(data);
        }
        return datas;
    }

    /**
     * Método que retorna uma lista de datas anteriores(limitadas ao dia atual e excluindo dias já passados) e
     * posteriores a um período entre datas informado
     *
     * @param dataInicial Data inicial do período
     * @param dataFinal   Data final do período
     * @return Lista contendo todas as datas anteriores(excluindo dia atual e dias já passados) e
     * posteriores a um período entre datas informado
     */
    public List<LocalDate> gerarDatasAnterioresEPosterioresAoPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        List<LocalDate> datasAnteriosresEPosterioresAoPeriodo = new ArrayList<>();

        //gerando dias anteriores a data inicial, se ela não for a data atual(hoje)
        if (dataInicial.isAfter(LocalDate.now())) {
            long diasAntesDaDataIncial = dataInicial.until(LocalDate.now(), ChronoUnit.DAYS);
            for (int i = 0; i > diasAntesDaDataIncial; i--) {
                LocalDate data = dataInicial.plusDays(i);
                datasAnteriosresEPosterioresAoPeriodo.add(data);
            }
        }
        //gerando dias posteriores a data final
        long diasDepoisDaDataFinal = 60;//60 dias a frente
        for (long i = diasDepoisDaDataFinal; i > 0; i--) {
            LocalDate data = dataFinal.plusDays(i);
            datasAnteriosresEPosterioresAoPeriodo.add(data);
        }
        return datasAnteriosresEPosterioresAoPeriodo.stream().sorted().collect(Collectors.toList());
    }

    /**
     * Método que retorna uma lista de datas contendo todos os dias anteriores a contar da dataInicial(exclusivo) até 31 dias antes
     *
     * @param dataInicial Data inicial do período
     * @return Lista contendo todas as datas iniciando da dataInical(exclusivo) até 30 dias antes
     */
    public List<LocalDate> gerarListaDeDatasUmMesAnteriorDaDataInicial(LocalDate dataInicial) {
        List<LocalDate> datas = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            LocalDate data = dataInicial.plusDays(-i);
            datas.add(data);
        }
        return datas;
    }

}
