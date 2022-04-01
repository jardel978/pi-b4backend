package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.FuncaoDTO;
import com.digitalbooking.projetointegrador.model.Funcao;
import com.digitalbooking.projetointegrador.repository.IFuncaoRepository;
import com.digitalbooking.projetointegrador.service.exception.DadoNaoEncontradoException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Classe de service para <strong>Funcao</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@Service
public class FuncaoService {

    @Autowired
    private IFuncaoRepository funcaoRepository;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE

    /**
     * Metodo que salva uma função no banco de dados.
     *
     * @param funcaoDTO Funcao que deve ser persistida no banco de dados.
     * @since 1.0
     */

    public void salvar(FuncaoDTO funcaoDTO) {
        Funcao funcaoModel = modelMapper.map(funcaoDTO, Funcao.class);
        funcaoRepository.save(funcaoModel);
    }


    // READ

    /**
     * Metodo para busca de todas as funcões.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Paginacao com funcoes já convertidas de Funcao para FuncaoDTO.
     * @since 1.0
     */

    public Page<FuncaoDTO> buscarTodos(Pageable pageable) {
        Page<Funcao> listaFuncoes = funcaoRepository.findAll(pageable);
        return listaFuncoes.map(funcaoModel -> modelMapper.map(funcaoModel, FuncaoDTO.class));

    }

    // PUT

    /**
     * Metodo para atualizar uma funcao.
     *
     * @param funcaoDTO Funcao a ser atualizada.
     * @since 1.0
     */

    public void atualizar(FuncaoDTO funcaoDTO) {
        funcaoRepository.findById(funcaoDTO.getId())
                .orElseThrow(() -> new DadoNaoEncontradoException("Função não encontrada." +
                        " Tipo: " + Funcao.class.getName()));
        salvar(funcaoDTO);
    }

    // DELETE

    /**
     * Metodo para deletar uma funcao.
     *
     * @param id Chave identificadora da funcao que deve ser deletada.
     * @since 1.0
     */

    public void deletar(Long id) {
        Funcao funcaoModel = funcaoRepository.findById(id)
                .orElseThrow(() -> new DadoNaoEncontradoException("Função não encontrada." +
                        " Tipo: " + Funcao.class.getName()));



        funcaoRepository.deleteById(id);
    }


}
