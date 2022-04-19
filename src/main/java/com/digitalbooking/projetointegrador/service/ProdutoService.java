package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.ProdutoDTO;
import com.digitalbooking.projetointegrador.model.*;
import com.digitalbooking.projetointegrador.repository.*;
import com.digitalbooking.projetointegrador.service.exception.DadoNaoEncontradoException;
import com.digitalbooking.projetointegrador.service.exception.DataIndisponivelReservaException;
import com.digitalbooking.projetointegrador.utils.DatasUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe de service para <strong>Produto</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@Service
public class ProdutoService {

    @Autowired
    private IProdutoRepository produtoRepository;

    @Autowired
    private ICidadeRepository cidadeRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private IImagemRepository imagemRepository;

    @Autowired
    private IDataReservadaRepository dataReservadaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DatasUtil datasUtil;

    /**
     * Metodo para salvar um produto.
     *
     * @param produtoDTO Produto que deve ser persistido no banco de dados.
     * @since 1.0
     */
    public void salvar(ProdutoDTO produtoDTO) {
        Produto produto = modelMapper.map(produtoDTO, Produto.class);

        Produto produtoModel = validarExistenciaDeCidadeECategoria(produto);
        produtoModel.setQtdAvaliacoes(0);
        produtoModel.setNotaDeAvaliacao(0.0);
        Produto produtoSalvo = produtoRepository.saveAndFlush(produtoModel);

        if (!produtoModel.getImagens().isEmpty()) {
            produtoModel.getImagens().forEach(imagem -> {
                imagem.setProduto(produtoSalvo);
                imagemRepository.save(imagem);
            });
        }
    }

    /**
     * Metodo que busca um produto por id.
     *
     * @param id Chave identificadora do produto que deve ser buscado.
     * @return Produto buscado.
     */
    public ProdutoDTO buscarPorId(Long id) {
        Produto produtoModel = produtoRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException(
                "Produto não encontrado. Tipo: " + Produto.class.getName()));

        return modelMapper.map(produtoModel, ProdutoDTO.class);
    }

    /**
     * Metodo que permite filtrar produtos por cidade e duas datas.
     *
     * @param nomeCidade  Nome da cidade a ser buscada.
     * @param dataInicial Data inicial a ser buscada.
     * @param dataFinal   Data final a ser buscada.
     * @param qtdPessoasPretendidas Quantidade de pessoas pretendidas pelo cliente para a busca
     * @return Produtos disponíveis encontrados.
     */
    public List<ProdutoDTO> buscarPorCidadeDatas(String nomeCidade, LocalDate dataInicial, LocalDate dataFinal,
                                                 Integer qtdPessoasPretendidas) {
        List<Produto> listaTotalDeProdutos = produtoRepository.findAllByCidadeNomeContainsIgnoreCase(nomeCidade);
        List<LocalDate> peridoDeDatas = datasUtil.gerarPeriodoDeDatas(dataInicial, dataFinal);
        List<Produto> produtosComDatasDisponiveis = new ArrayList<>();

        listaTotalDeProdutos.forEach(produto -> {//para cada produto
            List<LocalDate> listaDatasLivres = new ArrayList<>();

            if (qtdPessoasPretendidas <= produto.getLimitePessoasPorDia()) {
                peridoDeDatas.forEach(data -> {//verifique todas as datas
                    Optional<DataReservada> dataReservada = dataReservadaRepository.findById(new DataReservadaPK(data,
                            produto));
                    if (dataReservada.isPresent()) {//se data já possui reservas mas tem vagas suficientes
                        if (dataReservada.get().getQdtPessoasReservadas() + qtdPessoasPretendidas <= produto.getLimitePessoasPorDia()) {
                            listaDatasLivres.add(data);
                        }
                    } else {//se data não possui reservas ainda ela também está disponível
                        listaDatasLivres.add(data);
                    }
                });

                if (peridoDeDatas.size() == listaDatasLivres.size())//se todos as datas estão disponíveis
                    produtosComDatasDisponiveis.add(produto);//adicione o produto ao array
            } else {
                throw new DataIndisponivelReservaException("A quantidade de vagas solicitadas ultrapassa o limite de " +
                        "pessoas que esse camping suporta");
            }
        });

        produtosComDatasDisponiveis.stream().map(produto -> {
            List<LocalDate> datasAnteriosresEPosterioresAoPeriodo = datasUtil.gerarDatasAnterioresEPosterioresAoPeriodo(
                    dataInicial, dataFinal);
            List<LocalDate> datasIndisponiveis = new ArrayList<>();
            datasAnteriosresEPosterioresAoPeriodo.forEach(data -> {
                Optional<DataReservada> dataReservada = dataReservadaRepository.findById(new DataReservadaPK(data,
                        produto));
                if (dataReservada.isPresent()) {
                    if (dataReservada.get().getQdtPessoasReservadas() + qtdPessoasPretendidas > produto.getLimitePessoasPorDia()) {
                        datasIndisponiveis.add(data);
                    }
                }
            });
            produto.setDatasIndisponiveis(datasIndisponiveis);
            return produto;
        }).collect(Collectors.toList());

        return produtosComDatasDisponiveis.stream().map(produto ->
                modelMapper.map(produto, ProdutoDTO.class)).collect(Collectors.toList());
    }

    /**
     * Metodo para buscar todos os produtos por sua categoria.
     *
     * @param nomeCategoria Nome da categoria a ser buscada.
     * @return Lista com todos os produtos de uma determinada categoria.
     * @since 1.0
     */
    public List<ProdutoDTO> buscarPorCategoria(String nomeCategoria) {
        List<ProdutoDTO> listaProdutos = new ArrayList<>();

        produtoRepository.findAllByCategoriaNomeContainsIgnoreCase(nomeCategoria).forEach(produtoModel -> {
            ProdutoDTO produtoDTO = modelMapper.map(produtoModel, ProdutoDTO.class);
            listaProdutos.add(produtoDTO);
        });
        return listaProdutos;
    }

    /**
     * Metodo para buscar todos os produtos associados a uma cidade.
     *
     * @param nomeCidade Nome da cidade a ser buscada.
     * @return Lista com todos os produtos de uma determinada cidade.
     * @since 1.0
     */
    public List<ProdutoDTO> buscarPorCidade(String nomeCidade) {
        List<ProdutoDTO> listaProdutos = new ArrayList<>();

        produtoRepository.findAllByCidadeNomeContainsIgnoreCase(nomeCidade).forEach(produtoModel -> {
            ProdutoDTO produtoDTO = modelMapper.map(produtoModel, ProdutoDTO.class);
            listaProdutos.add(produtoDTO);
        });
        return listaProdutos;
    }

    /**
     * Metodo que faz a busca de todos os produtos.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Paginacao com produtos já convertidos de Produto para ProdutoDTO.
     * @since 1.0
     */
    public Page<ProdutoDTO> buscarTodos(Pageable pageable) {
        Page<Produto> pageProdutos = produtoRepository.findAll(pageable);
        return pageProdutos.map(produtoModel -> modelMapper.map(produtoModel, ProdutoDTO.class));
    }

    /**
     * Metodo para atualizar um produto.
     *
     * @param produtoDTO Produto a ser atualizado.
     * @since 1.0
     */
    public void atualizar(ProdutoDTO produtoDTO) {
        Produto produto = modelMapper.map(produtoDTO, Produto.class);

        Produto produtoDaBase =
                produtoRepository.findById(produtoDTO.getId()).orElseThrow(() -> new DadoNaoEncontradoException(
                        "Produto não encontrado. Tipo: " + Produto.class.getName()));

        produtoDaBase.getImagens().forEach(imagemDaBase -> {//verificando alterações nas imagens do produto
            if (!produto.getImagens().contains(imagemDaBase)) {//remove as imagens que não fazem mais relação
                // com o produto que está sendo atualizado
                imagemDaBase.setProduto(null);
                imagemRepository.save(imagemDaBase);
                imagemRepository.deleteById(imagemDaBase.getId());
            }
        });
        Produto produtoModel = validarExistenciaDeCidadeECategoria(produto);
        produtoModel.setQtdAvaliacoes(produtoDaBase.getQtdAvaliacoes());
        produtoModel.setNotaDeAvaliacao(produtoDaBase.getNotaDeAvaliacao());

        Produto produtoAtualizado = produtoRepository.saveAndFlush(produtoModel);
        if (!produtoAtualizado.getImagens().isEmpty()) {
            produtoModel.getImagens().forEach(imagem -> {
                imagem.setProduto(produtoAtualizado);
                imagemRepository.save(imagem);
            });
        }
    }

    /**
     * Metodo para deletar uma produto.
     *
     * @param id Chave identificadora do produto que deve ser deletado.
     * @since 1.0
     */
    public void deletar(Long id) {
        Produto produtoModel = produtoRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException("Produto não " +
                "encontrado. Tipo: " + Produto.class.getName()));

        if (!produtoModel.getReservas().isEmpty()) {
            produtoModel.getReservas().forEach(reserva -> {
                if (reserva.getDataFinal().isBefore(LocalDate.now())) {//se data final da reserva já passou
                    reservaRepository.deleteById(reserva.getId());
                } else {
                    throw new DadoNaoEncontradoException("Esse produto não pode ser excluído pois está relacionado a " +
                            "uma ou mais reservas");
                }
            });
        }
        produtoRepository.deleteById(id);
    }

    private Produto validarExistenciaDeCidadeECategoria(Produto produto) {//se não exitam, ele cria e seta ao produto
        if (produto.getCidade().getId() == null) {
            Cidade cidadeModel = cidadeRepository.saveAndFlush(produto.getCidade());
            produto.setCidade(cidadeModel);
        } else {
            cidadeRepository.findById(produto.getCidade().getId()).orElseThrow(() -> new DadoNaoEncontradoException(
                    "Cidade não encontrada. Tipo: " + Cidade.class.getName()));
        }
        if (produto.getCategoria().getId() == null) {
            Categoria categoriaModel = categoriaRepository.saveAndFlush(produto.getCategoria());
            produto.setCategoria(categoriaModel);
        } else {
            categoriaRepository.findById(produto.getCategoria().getId()).orElseThrow(() -> new DadoNaoEncontradoException(
                    "Categoria não encontrada. Tipo: " + Categoria.class.getName()));
        }
        return produto;
    }

}
