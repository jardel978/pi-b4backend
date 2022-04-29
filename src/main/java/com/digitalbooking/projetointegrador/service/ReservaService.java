package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.OrdemDePagamentoMixReservaDTO;
import com.digitalbooking.projetointegrador.dto.PagamentoReservaDTO;
import com.digitalbooking.projetointegrador.dto.ReservaDTO;
import com.digitalbooking.projetointegrador.dto.UsuarioMixReservaDTO;
import com.digitalbooking.projetointegrador.model.*;
import com.digitalbooking.projetointegrador.model.enums.StatusReserva;
import com.digitalbooking.projetointegrador.repository.*;
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

    @Autowired
    private IOrdemPagamentoRepository ordemPagamentoRepository;

    //CREATE

    /**
     * Metodo que salva uma reserva no banco de dados.
     *
     * @param reservaDTO Reserva que deve ser persistida no banco de dados.
     * @since 1.0
     */
    public ReservaDTO salvar(ReservaDTO reservaDTO) {
        Reserva reservaModel = modelMapper.map(reservaDTO, Reserva.class);
        //mapeando/convertendo um objeto do tipo ReservaDTO para Reserva
        Usuario usuario = usuarioRepository.findById(reservaDTO.getUsuario().getId()).orElseThrow(() ->
                new DadoNaoEncontradoException("O usuário informado para a reserva não foi encontrado. Tipo: " + Usuario.class.getName()));
        Produto produto = produtoRepository.findById(reservaDTO.getProduto().getId()).orElseThrow(() ->
                new DadoNaoEncontradoException("O produto informado para a reserva não foi encontrado. Tipo: " + Produto.class.getName()));
        List<LocalDate> peridoDeDatas = datasUtil.gerarPeriodoDeDatas(reservaDTO.getDataInicio(), reservaDTO.getDataFinal());
        List<LocalDate> possiveisDatasIndisponiveis = new ArrayList<>();

        if (reservaDTO.getQtdPessoas() <= produto.getLimitePessoasPorDia()) {//se número de vagas que a reserva pede for no máximo o limite de vagas do camping
            peridoDeDatas.forEach(data -> {
                Optional<DataReservada> dataReservada = dataReservadaRepository.findById(new DataReservadaPK(data, produto));
                if (dataReservada.isPresent()) {
                    boolean vagasDisponiveisComportamTodos =//quantidade de vagas disponíveis for maior ou igual a quantidade de vagas que a reserva pede
                            (produto.getLimitePessoasPorDia() - dataReservada.get().getQdtPessoasReservadas()) >= reservaDTO.getQtdPessoas();

                    if (!vagasDisponiveisComportamTodos) {//se data indisponível
                        possiveisDatasIndisponiveis.add(data);
                    }
                }
            });
        } else
            throw new RegraDeNegocioVioladaException("Falha ao gerar reserva! A quantidade de pessoas informada " +
                    "excede o limite diário de pessoas no camping. " + "Limite diário: " + produto.getLimitePessoasPorDia() + " - Quantidade " +
                    "informada: " + reservaDTO.getQtdPessoas());

        if (!possiveisDatasIndisponiveis.isEmpty())
            throw new DataIndisponivelReservaException("O intervalo de datas escolhido possui dias indisponíveis. " +
                    "Datas indisponíveis: " + possiveisDatasIndisponiveis);

        reservaModel.setCliente((Cliente) usuario);
        reservaModel.setStatus(StatusReserva.PENDENTE);

        UsuarioMixReservaDTO usuarioMixReservaDTO = modelMapper.map(usuario, UsuarioMixReservaDTO.class);
        ReservaDTO reservaDTO1 = modelMapper.map(reservaRepository.saveAndFlush(reservaModel), ReservaDTO.class);
        reservaDTO1.setUsuario(usuarioMixReservaDTO);
        return reservaDTO1;
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
            if (reservaModel.getStatus().equals(StatusReserva.PAGO) && reservaModel.getDataFinal().isBefore(LocalDate.now())) {
                reservaModel.setStatus(StatusReserva.FECHADO);
                reservaRepository.save(reservaModel);
            }
            OrdemDePagamentoMixReservaDTO ordemPagamentoDTO = null;
            if (reservaModel.getOrdemDePagamento() != null)
                ordemPagamentoDTO = modelMapper.map(reservaModel.getOrdemDePagamento(), OrdemDePagamentoMixReservaDTO.class);

            ReservaDTO reservaDTO = modelMapper.map(reservaModel, ReservaDTO.class);
            reservaDTO.setOrdemDePagamento(ordemPagamentoDTO);
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
                    return listaReservas.stream().map(reservaModel -> {
                        if (reservaModel.getStatus().equals(StatusReserva.PAGO) && reservaModel.getDataFinal().isBefore(LocalDate.now())) {
                            reservaModel.setStatus(StatusReserva.FECHADO);
                            reservaRepository.save(reservaModel);
                        }
                        OrdemDePagamentoMixReservaDTO ordemPagamentoDTO = null;
                        if (reservaModel.getOrdemDePagamento() != null)
                            ordemPagamentoDTO = modelMapper.map(reservaModel.getOrdemDePagamento(), OrdemDePagamentoMixReservaDTO.class);

                        ReservaDTO reservaDTO = modelMapper.map(reservaModel, ReservaDTO.class);
                        reservaDTO.setOrdemDePagamento(ordemPagamentoDTO);
                        return reservaDTO;
                    }).collect(Collectors.toList());
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
     * @param pagamentoReservaDTO Reserva a ser atualizada seu status.
     * @since 1.0
     */
    public void atualizar(PagamentoReservaDTO pagamentoReservaDTO) {
        Reserva reservaDaBase = reservaRepository.findById(pagamentoReservaDTO.getReservaId()).orElseThrow(() ->
                new DadoNaoEncontradoException("Reserva não encontrada. Tipo: " + Reserva.class.getName()));

        OrdemDePagamento ordemDePagamento =
                ordemPagamentoRepository.findById(pagamentoReservaDTO.getOrdemDePagamentoId()).orElseThrow(() ->
                        new DadoNaoEncontradoException("Falha ao atualizar reserva. Ordem de pagamento não encontrada. Tipo:" + " " + Reserva.class.getName()));

        if (reservaDaBase.getStatus().equals(StatusReserva.PENDENTE) &&
                ordemDePagamento.getReserva().getId().equals(pagamentoReservaDTO.getReservaId())) {
            List<LocalDate> peridoDeDatas = datasUtil.gerarPeriodoDeDatas(reservaDaBase.getDataInicio(), reservaDaBase.getDataFinal());
            List<LocalDate> possiveisDatasIndisponiveis = new ArrayList<>();
            List<DataReservada> listaDatasReservadasLivres = new ArrayList<>();

            if (reservaDaBase.getQtdPessoas() <= reservaDaBase.getProduto().getLimitePessoasPorDia()) {//se número de
                // vagas que a reserva pede for no máximo o limite de vagas do camping
                peridoDeDatas.forEach(data -> {
                    Optional<DataReservada> dataReservada = dataReservadaRepository.findById(new DataReservadaPK(data
                            , reservaDaBase.getProduto()));
                    if (dataReservada.isPresent()) {
                        boolean vagasDisponiveisComportamTodos =//quantidade de vagas disponíveis for maior ou igual a quantidade de vagas que a reserva pede
                                (reservaDaBase.getProduto().getLimitePessoasPorDia() - dataReservada.get().getQdtPessoasReservadas()) >= reservaDaBase.getQtdPessoas();

                        if (vagasDisponiveisComportamTodos) {//se data disponível
                            dataReservada.get().setQdtPessoasReservadas(//somando a quantidade de pessoas existentes mais as novas para a nova reserva
                                    dataReservada.get().getQdtPessoasReservadas() + reservaDaBase.getQtdPessoas());
                            listaDatasReservadasLivres.add(dataReservada.get());
                        } else {
                            possiveisDatasIndisponiveis.add(data);
                        }
                    } else {
                        DataReservadaPK id = new DataReservadaPK(data, reservaDaBase.getProduto());
                        listaDatasReservadasLivres.add(new DataReservada(id, reservaDaBase.getQtdPessoas()));
                    }
                });
            } else
                throw new RegraDeNegocioVioladaException("Falha ao gerar reserva! A quantidade de pessoas informada " +
                        "excede o limite diário de pessoas no camping. " + "Limite diário: " + reservaDaBase.getProduto().getLimitePessoasPorDia() + " - Quantidade " +
                        "informada: " + reservaDaBase.getQtdPessoas());

            if (possiveisDatasIndisponiveis.isEmpty()) {
                listaDatasReservadasLivres.forEach(data -> dataReservadaRepository.save(data));
                reservaDaBase.setStatus(StatusReserva.PAGO);
                reservaRepository.save(reservaDaBase);
            } else {
                reservaDaBase.setStatus(StatusReserva.CANCELADO);
                reservaRepository.save(reservaDaBase);
                throw new DataIndisponivelReservaException("Reserva cancelada pois o intervalo de datas escolhido não" +
                        " se encontra mais disponível. Datas indisponíveis: " + possiveisDatasIndisponiveis);
            }
        }

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
