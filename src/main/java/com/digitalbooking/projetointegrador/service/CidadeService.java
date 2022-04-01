package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.CidadeDTO;
import com.digitalbooking.projetointegrador.model.Cidade;
import com.digitalbooking.projetointegrador.repository.ICidadeRepository;
import com.digitalbooking.projetointegrador.service.exception.DadoNaoEncontradoException;
import com.digitalbooking.projetointegrador.service.exception.DadoRelacionadoException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Classe de service para <strong>Cidade</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@Service
public class CidadeService {

    @Autowired
    private ICidadeRepository cidadeRepository;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE

    /**
     * Metodo que salva uma cidade no banco de dados.
     *
     * @param cidadeDTO Cidade que deve ser persistida no banco de dados.
     * @since 1.0
     */

    public void salvar(CidadeDTO cidadeDTO) {
        Cidade cidadeModel = modelMapper.map(cidadeDTO, Cidade.class);
        cidadeRepository.save(cidadeModel);
    }


    // READ

    /**
     * Metodo para busca de todas as cidades.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Paginacao com cidades já convertidas de Cidade para CidadeDTO.
     * @since 1.0
     */

    public Page<CidadeDTO> buscarTodos(Pageable pageable) {
        Page<Cidade> listaCidades = cidadeRepository.findAll(pageable);
        return listaCidades.map(cidadeModel -> modelMapper.map(cidadeModel, CidadeDTO.class));
    }


    // PUT

    /**
     * Metodo para atualizar uma cidade.
     *
     * @param cidadeDTO Cidade a ser atualizada.
     * @since 1.0
     */

    public void atualizar(CidadeDTO cidadeDTO) {
        cidadeRepository.findById(cidadeDTO.getId())
                .orElseThrow(() -> new DadoNaoEncontradoException("Cidade não encontrada." +
                        " Tipo: " + Cidade.class.getName()));
        salvar(cidadeDTO);
    }

    // DELETE

    /**
     * Metodo para deletar uma cidade.
     *
     * @param id Chave identificadora da cidade que deve ser deletada.
     * @since 1.0
     */

    public void deletar(Long id) {
        Cidade cidadeModel = cidadeRepository.findById(id)
                .orElseThrow(() -> new DadoNaoEncontradoException("Cidade não encontrada. " +
                        " Tipo: " + Cidade.class.getName()));

        if (!cidadeModel.getProdutos().isEmpty())
            throw new DadoRelacionadoException("A cidade [ " + cidadeModel.getNome() + " ] não pode ser " +
                    "excluída pois está relacionada com um ou mais produtos");

        cidadeRepository.deleteById(id);
    }

}
