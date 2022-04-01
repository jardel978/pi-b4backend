package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.ProdutoDTO;
import com.digitalbooking.projetointegrador.model.Categoria;
import com.digitalbooking.projetointegrador.model.Cidade;
import com.digitalbooking.projetointegrador.model.Produto;
import com.digitalbooking.projetointegrador.repository.ICategoriaRepository;
import com.digitalbooking.projetointegrador.repository.ICidadeRepository;
import com.digitalbooking.projetointegrador.repository.IImagemRepository;
import com.digitalbooking.projetointegrador.repository.IProdutoRepository;
import com.digitalbooking.projetointegrador.service.exception.DadoNaoEncontradoException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

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
    private IImagemRepository imagemRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Metodo para salvar um produto.
     *
     * @param produtoDTO Produto que deve ser persistido no banco de dados.
     * @since 1.0
     */
    public void salvar(ProdutoDTO produtoDTO) {
        Produto produtoModel = modelMapper.map(produtoDTO, Produto.class);

        if (produtoDTO.getCidade().getId() == null) {
            Cidade cidadeModel = cidadeRepository.saveAndFlush(produtoModel.getCidade());
            produtoModel.setCidade(cidadeModel);
        }
        if (produtoDTO.getCategoria().getId() == null) {
            Categoria categoriaModel = categoriaRepository.saveAndFlush(produtoModel.getCategoria());
            produtoModel.setCategoria(categoriaModel);
        }

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
    @GetMapping("/buscar/{id}")
    public ProdutoDTO buscarPorId(Long id) {
        Produto produtoModel = produtoRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException(
                "Produto não encontrado. Tipo: " + Produto.class.getName()));

        return modelMapper.map(produtoModel, ProdutoDTO.class);
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
        Produto produtoModel = modelMapper.map(produtoDTO, Produto.class);

        Produto produtoDaBase =
                produtoRepository.findById(produtoDTO.getId()).orElseThrow(() -> new DadoNaoEncontradoException(
                        "Produto não encontrado. Tipo: " + Produto.class.getName()));

        produtoDaBase.getImagens().forEach(imagemDaBase -> {//verificando alterações nas imagens do produto
            if (!produtoModel.getImagens().contains(imagemDaBase)) {//remove as imagens que não fazem mais relação
                // com o produto que está sendo atualizado
                imagemDaBase.setProduto(null);
                imagemRepository.save(imagemDaBase);
                imagemRepository.deleteById(imagemDaBase.getId());
            }
        });
        salvar(produtoDTO);
    }

    /**
     * Metodo para deletar uma produto.
     *
     * @param id Chave identificadora do produto que deve ser deletado.
     * @since 1.0
     */
    public void deletar(Long id) {
        Produto produtoModel =produtoRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException("Produto não " +
                "encontrado." +
                " Tipo: " + Produto.class.getName()));

        if(produtoModel.getReservas() != null)
            throw new DadoNaoEncontradoException("Esse Produto não pode ser excluído pois está relacionado a uma reserva");

        produtoRepository.deleteById(id);
    }
}
