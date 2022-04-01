package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.ReservaDTO;
import com.digitalbooking.projetointegrador.model.Reserva;
import com.digitalbooking.projetointegrador.repository.IReservaRepository;
import com.digitalbooking.projetointegrador.service.exception.DadoNaoEncontradoException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe de service para <strong>Reserva</strong>.
 *
 * @version 1.0
 * @since 1.0
 */
@Service
public class ReservaService {

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private ModelMapper modelMapper;

    //CREATE

    /**
     * Metodo que salva uma reserva no banco de dados.
     *
     * @param reservaDTO Reserva que deve ser persistida no banco de dados.
     * @since 1.0
     */
    public void salvar(ReservaDTO reservaDTO) {
        Reserva reservaModel = modelMapper.map(reservaDTO, Reserva.class);
        //mapeando/convertendo um objeto do tipo ReservaDTO para Reserva
        reservaRepository.save(reservaModel);
    }

    //READ

    /**
     * Metodo para busca de todas as reservas.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Paginacao com reservas já convertidas de Reserva para ReservaDTO.
     * @since 1.0
     */
    public Page<ReservaDTO> buscarTodos(Pageable pageable) {
        Page<Reserva> listaReservas = reservaRepository.findAll(pageable);
        return listaReservas.map(reservaModel -> modelMapper.map(reservaModel, ReservaDTO.class));
    }

    /**
     * Metodo para busca de todas as reservas de um determina cliente.
     *
     * @param emailCliente Email do cliente o qual queremos retormar todas as suas reservas
     * @return Lista com todas as reservas de um determinado cliente já convertidas de Reserva para ReservaDTO.
     * @since 1.0
     */
    public List<ReservaDTO> buscarTodosDeUmCliente(String emailCliente) {
        List<Reserva> listaReservas = reservaRepository.findAllByClienteEmail(emailCliente);
        return listaReservas.stream().map(reservaModel -> modelMapper.map(reservaModel, ReservaDTO.class)).collect(Collectors.toList());
    }

    //UPDATE

    /**
     * Metodo para atualizar uma Reserva.
     *
     * @param reservaDTO Reserva a ser atualizada.
     * @since 1.0
     */
    public void atualizar(ReservaDTO reservaDTO) {
        reservaRepository.findById(reservaDTO.getId())
                .orElseThrow(() -> new DadoNaoEncontradoException("Reserva não encontrada." +
                        " Tipo: " + Reserva.class.getName()));
        salvar(reservaDTO);
    }

    //DELETE

    /**
     * Metodo para deletar uma Reserva.
     *
     * @param id Chave identificadora da Reserva que deve ser deletada.
     * @since 1.0
     */
    public void deletar(Long id) {
        Reserva reservaModel = reservaRepository.findById(id)
                .orElseThrow(() -> new DadoNaoEncontradoException("Reserva não encontrada." +
                        " Tipo: " + Reserva.class.getName()));
        reservaRepository.deleteById(id);
    }

}
