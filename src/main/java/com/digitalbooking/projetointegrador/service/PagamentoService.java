package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.OrdemDePagamentoDTO;
import com.digitalbooking.projetointegrador.dto.OrdemDePagamentoSummaryDTO;
import com.digitalbooking.projetointegrador.model.OrdemDePagamento;
import com.digitalbooking.projetointegrador.model.Reserva;
import com.digitalbooking.projetointegrador.repository.IOrdemPagamentoRepository;
import com.digitalbooking.projetointegrador.repository.IReservaRepository;
import com.digitalbooking.projetointegrador.service.exception.DadoNaoEncontradoException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class PagamentoService {

    private Map<String, String> environmentsMap = System.getenv();
    private String stripeSecret = environmentsMap.get("STRIPE_SECRET_KEY");

    @Autowired
    private IOrdemPagamentoRepository ordemPagamentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IReservaRepository reservaRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecret;
    }

    public OrdemDePagamentoSummaryDTO carregarOrdemDePagamento(OrdemDePagamentoDTO ordemDTO) throws StripeException {
        OrdemDePagamento ordemDePagamentoModel = modelMapper.map(ordemDTO, OrdemDePagamento.class);
        reservaRepository.findById(ordemDTO.getReserva().getId()).orElseThrow(() ->
                new DadoNaoEncontradoException("Reserva não encontrada. Tipo: " + Reserva.class.getName()));
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("amount", ordemDTO.getValor());
        parametros.put("currency", ordemDTO.getMoeda());
        parametros.put("description", ordemDTO.getDescricao());
        parametros.put("source", ordemDTO.getTokenId());

        Charge charge = Charge.create(parametros);

        if (!charge.getId().equals(null)) {
            ordemDePagamentoModel.setTransacaoId(charge.getId());
            ordemDePagamentoModel = ordemPagamentoRepository.saveAndFlush(ordemDePagamentoModel);
        }
        return OrdemDePagamentoSummaryDTO.builder()
                .id(ordemDePagamentoModel.getId())
                .transacaoId(ordemDePagamentoModel.getTransacaoId()).build();

    }

}
