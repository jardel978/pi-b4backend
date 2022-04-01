package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.CaracteristicaDTO;
import com.digitalbooking.projetointegrador.model.Caracteristica;
import com.digitalbooking.projetointegrador.repository.ICaracteristicaRepository;
import com.digitalbooking.projetointegrador.service.exception.DadoNaoEncontradoException;
import com.digitalbooking.projetointegrador.service.exception.DadoRelacionadoException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Classe de service para <strong>Caracteristica</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@Service
public class CaracteristicaService {

    @Autowired
    private ICaracteristicaRepository caracteristicaRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Metodo que salva uma caracteristica no banco de dados.
     *
     * @param caracteristicaDTO Categoria que deve ser persistida no banco de dados.
     * @since 1.0
     */
    //CREATE
    public void salvar(CaracteristicaDTO caracteristicaDTO) {
        Caracteristica caracteristicaModel = modelMapper.map(caracteristicaDTO, Caracteristica.class);
        caracteristicaRepository.save(caracteristicaModel);
    }


    //READ

    /**
     * Metodo para busca de todas as caracteristicas.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Paginacao com caracteristica já convertidas de Caracteristica para CaracteristicaDTO.
     * @since 1.0
     */
    public Page<CaracteristicaDTO> buscarTodos(Pageable pageable) {
        Page<Caracteristica> pageCaracteristicas = caracteristicaRepository.findAll(pageable);
        return pageCaracteristicas.map(caracteristicaModel -> modelMapper.map(caracteristicaModel,
                CaracteristicaDTO.class));
    }


    //UPDATE

    /**
     * Metodo para atualizar uma caracteristica.
     *
     * @param caracteristicaDTO Caracteristica a ser atualizada.
     * @since 1.0
     */
    public void atualizar(CaracteristicaDTO caracteristicaDTO) {
        caracteristicaRepository.findById(caracteristicaDTO.getId())
                .orElseThrow(() -> new DadoNaoEncontradoException("Caracteristica não encontrada." +
                        " Tipo: " + Caracteristica.class.getName()));
        salvar(caracteristicaDTO);
    }

    //DELETE

    /**
     * Metodo para deletar uma caracteristica.
     *
     * @param id Chave identificadora da caracteristica que deve ser deletada.
     * @since 1.0
     */
    public void deletar(Long id) {
        Caracteristica caracteristicaModel = caracteristicaRepository.findById(id)
                .orElseThrow(() -> new DadoNaoEncontradoException("Caracteristica não encontrada." +
                        " Tipo: " + Caracteristica.class.getName()));

        if (!caracteristicaModel.getProdutos().isEmpty())
            throw new DadoRelacionadoException("A característica [ " + caracteristicaModel.getNome() +
                    " ] não pode ser excluída pois está relacionada com um ou mais produtos");

        caracteristicaRepository.deleteById(id);
    }


}
