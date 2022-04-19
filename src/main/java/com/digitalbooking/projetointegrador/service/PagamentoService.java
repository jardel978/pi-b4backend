package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.OrdemDePagamentoDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class PagamentoService {

    private Map<String, String> environmentsMap = System.getenv();
    private String stripeSecret = environmentsMap.get("STRIPE_SECRET_KEY");

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecret;
    }

    public String carregarOrdemDePagamento(OrdemDePagamentoDTO ordemDTO) throws StripeException {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("amount", ordemDTO.getValor());
        parametros.put("currency", ordemDTO.getMoeda());
        parametros.put("description", ordemDTO.getDescricao());
        parametros.put("source", ordemDTO.getToken());

        Charge charge = Charge.create(parametros);
        return charge.getId();
    }

}
