package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.ScoreDTO;
import com.digitalbooking.projetointegrador.model.*;
import com.digitalbooking.projetointegrador.repository.IClienteRepository;
import com.digitalbooking.projetointegrador.repository.IProdutoRepository;
import com.digitalbooking.projetointegrador.repository.IScoreRepository;
import com.digitalbooking.projetointegrador.service.exception.DadoNaoEncontradoException;
import com.digitalbooking.projetointegrador.service.exception.RegraDeNegocioVioladaException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe de service para <strong>Score</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@Service
public class ScoreService {

    @Autowired
    private IScoreRepository scoreRepository;

    @Autowired
    private IProdutoRepository produtoRepository;

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public void salvar(ScoreDTO scoreDTO) {

        Cliente cliente = clienteRepository.findById(scoreDTO.getIdCliente()).orElseThrow(() -> new DadoNaoEncontradoException(
                "Falha ao adicionar score! Cliente não encontrado. Tipo: " + Cliente.class.getName()));

        Produto produto = produtoRepository.findById(scoreDTO.getIdProduto()).orElseThrow(() -> new DadoNaoEncontradoException(
                "Falha ao adicionar score! Produto não encontrado. Tipo: " + Produto.class.getName()));


        if (verificarListaDeReservas(cliente, produto)) {
            Score novoScore = Score.builder()
                    .id(new ScorePK(produto, cliente))
                    .valor(scoreDTO.getValor())
                    .build();

            scoreRepository.saveAndFlush(novoScore);

            double somaTotalScores = 0.0;
            for (Score score : produto.getScores()) {//percorre todos os scores que o produto já tinha
                somaTotalScores = somaTotalScores + score.getValor();
            }

            double mediaDasAvalicaoes = somaTotalScores / produto.getScores().size();//média aritmética dos scores
            // que o produto tinha mais o novo que foi gerado
            produto.setNotaDeAvaliacao(mediaDasAvalicaoes);
            produto.setQtdAvaliacoes(produto.getScores().size());
            produtoRepository.save(produto);
        } else
            throw new RegraDeNegocioVioladaException("Falha ao salvar avaliação do produto: " + produto.getNome()
                    + ". O cliente " + cliente.getNome() + " " + cliente.getSobrenome() + " não possui nenhuma " +
                    "reserva em seu histórico que tenha sido efetuado para esse produto! Isso impossibilita a " +
                    "avaliação.");
    }

    private boolean verificarListaDeReservas(Cliente cliente, Produto produto) {
        for (Reserva reservaCliente : cliente.getReservas()) {
            if (produto.getReservas().contains(reservaCliente))
                return true;
        }
        return false;
    }

}
