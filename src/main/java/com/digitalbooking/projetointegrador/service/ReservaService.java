package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.ReservaDTO;
import com.digitalbooking.projetointegrador.dto.UsuarioMixReservaDTO;
import com.digitalbooking.projetointegrador.model.*;
import com.digitalbooking.projetointegrador.repository.IDataReservadaRepository;
import com.digitalbooking.projetointegrador.repository.IProdutoRepository;
import com.digitalbooking.projetointegrador.repository.IReservaRepository;
import com.digitalbooking.projetointegrador.repository.IUsuarioRepository;
import com.digitalbooking.projetointegrador.security.JwtUtil;
import com.digitalbooking.projetointegrador.security.exception.GenericoTokenException;
import com.digitalbooking.projetointegrador.service.exception.*;
import com.digitalbooking.projetointegrador.utils.DatasUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.digitalbooking.projetointegrador.security.JwtFiltroValidacao.ATRIBUTO_PREFIXO;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
    private IDataReservadaRepository dataReservadaRepository;

    @Autowired
    private IProdutoRepository produtoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DatasUtil datasUtil;

    @Autowired
    private JwtUtil jwtUtil;

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
        Usuario usuario = usuarioRepository.findById(reservaDTO.getUsuario().getId()).orElseThrow(() ->
                new DadoNaoEncontradoException("O usuário informado para a reserva não foi encontrado. Tipo: " + Usuario.class.getName()));
        Produto produto = produtoRepository.findById(reservaDTO.getProduto().getId()).orElseThrow(() ->
                new DadoNaoEncontradoException("O produto informado para a reserva não foi encontrado. Tipo: " + Produto.class.getName()));
        List<LocalDate> peridoDeDatas = datasUtil.gerarPeriodoDeDatas(reservaDTO.getDataInicio(), reservaDTO.getDataFinal());
        List<LocalDate> possiveisDatasIndisponiveis = new ArrayList<>();
        List<DataReservada> listaDatasReservadasLivres = new ArrayList<>();

        if (reservaDTO.getQtdPessoas() <= produto.getLimitePessoasPorDia()) {//se número de vagas que a reserva pede for no máximo o limite de vagas do camping
            peridoDeDatas.forEach(data -> {
                Optional<DataReservada> dataReservada = dataReservadaRepository.findById(new DataReservadaPK(data, produto));
                if (dataReservada.isPresent()) {
                    boolean vagasDisponiveisComportamTodos =//quantidade de vagas disponíveis for maior ou igual a quantidade de vagas que a reserva pede
                            (produto.getLimitePessoasPorDia() - dataReservada.get().getQdtPessoasReservadas()) >= reservaDTO.getQtdPessoas();

                    if (vagasDisponiveisComportamTodos) {//se data disponível
                        dataReservada.get().setQdtPessoasReservadas(//somando a quantidade de pessoas existentes mais as novas para a nova reserva
                                dataReservada.get().getQdtPessoasReservadas() + reservaDTO.getQtdPessoas());
                        listaDatasReservadasLivres.add(dataReservada.get());
                    } else {
                        possiveisDatasIndisponiveis.add(data);
                    }
                } else {
                    DataReservadaPK id = new DataReservadaPK(data, produto);
                    listaDatasReservadasLivres.add(new DataReservada(id, reservaDTO.getQtdPessoas()));
                }
            });
        } else
            throw new RegraDeNegocioVioladaException("Falha ao gerar reserva! A quantidade de pessoas informada " +
                    "excede o limite diário de pessoas no camping. " + "Limite diário: " + produto.getLimitePessoasPorDia() + " - Quantidade " +
                    "informada: " + reservaDTO.getQtdPessoas());

        if (possiveisDatasIndisponiveis.isEmpty()) {
            listaDatasReservadasLivres.forEach(data -> dataReservadaRepository.save(data));
            reservaModel.setCliente((Cliente) usuario);
            reservaRepository.save(reservaModel);
        } else
            throw new DataIndisponivelReservaException("O intervalo de datas escolhido possui dias indisponíveis. " +
                    "Datas indisponíveis: " + possiveisDatasIndisponiveis);
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
        return listaReservas.map(reservaModel -> {
            UsuarioMixReservaDTO usuarioMixReservaDTO = modelMapper.map(//gerando UsuarioMixReservaDTO para setar na reserva
                    reservaModel.getCliente(), UsuarioMixReservaDTO.class);
            ReservaDTO reservaDTO = modelMapper.map(reservaModel, ReservaDTO.class);
            reservaDTO.setUsuario(usuarioMixReservaDTO);
            return reservaDTO;
        });
    }

    /**
     * Metodo para busca de todas as reservas de um determina cliente.
     *
     * @param emailCliente Email do cliente o qual queremos retornar todas as suas reservas
     * @return Lista com todas as reservas de um determinado cliente já convertidas de Reserva para ReservaDTO.
     * @since 1.0
     */
    public List<ReservaDTO> buscarTodosDeUmCliente(HttpServletRequest request, String emailCliente) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(ATRIBUTO_PREFIXO)) {
            if (jwtUtil.validarToken(authorizationHeader)) {
                String emailToken = jwtUtil.pegarEmailUsuarioViaToken(authorizationHeader);
                if (emailToken.equals(emailCliente)) {
                    List<Reserva> listaReservas = reservaRepository.findAllByClienteEmail(emailCliente);
                    return listaReservas.stream().map(reservaModel -> modelMapper.map(reservaModel, ReservaDTO.class)).collect(Collectors.toList());
                } else
                    throw new RegraDeSegurancaVioladaException("Falha ao buscar reservas do cliente com email: " +
                            emailCliente + ". O email informado não condiz com o email encontrado no token de segurança.");
            } else {
                String erroEmToken = jwtUtil.capturarErroEmToken(authorizationHeader);
                throw new GenericoTokenException(erroEmToken);
            }
        } else {
            throw new RegraDeSegurancaVioladaException("É necessário enviar um token para validar o email informado");
        }
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

        if (reservaModel.getDataFinal().isAfter(LocalDate.now())) {//se a data final da reserva ainda não passou
            throw new ReservaNaoFinalizadaException("Falha ao excluir reserva! A data de fechamento dessa reserva é " +
                    "posterior ao dia de hoje. Não é possível excluir uma reserva que ainda não foi finalizada.");
        }
        reservaRepository.deleteById(id);
    }

}
