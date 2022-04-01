package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.CategoriaDTO;
import com.digitalbooking.projetointegrador.model.Categoria;
import com.digitalbooking.projetointegrador.repository.ICategoriaRepository;
import com.digitalbooking.projetointegrador.service.exception.DadoNaoEncontradoException;
import com.digitalbooking.projetointegrador.service.exception.DadoRelacionadoException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Classe de service para <strong>Categoria</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@Service
public class CategoriaService {

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Metodo que salva uma categoria no banco de dados.
     *
     * @param categoriaDTO Categoria que deve ser persistida no banco de dados.
     * @since 1.0
     */
    public void salvar(CategoriaDTO categoriaDTO) {
        Categoria categoriaModel = modelMapper.map(categoriaDTO, Categoria.class);//mapeando/convertendo um objeto do
        //tipo CategoriaDTO para Categoria
        categoriaRepository.save(categoriaModel);
    }

    /**
     * Metodo para busca de todas as categorias.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Paginacao com categorias já convertidas de Categoria para CategoriaDTO.
     * @since 1.0
     */
    public Page<CategoriaDTO> buscarTodos(Pageable pageable) {
        Page<Categoria> pageCategorias = categoriaRepository.findAll(pageable);
        return pageCategorias.map(categoriaModel -> modelMapper.map(categoriaModel, CategoriaDTO.class));
    }

    /**
     * Metodo para atualizar uma categoria.
     *
     * @param categoriaDTO Categoria a ser atualizada.
     * @since 1.0
     */
    public void atualizar(CategoriaDTO categoriaDTO) {
        categoriaRepository.findById(categoriaDTO.getId())
                .orElseThrow(() -> new DadoNaoEncontradoException("Categoria não encontrada." +
                        " Tipo: " + Categoria.class.getName()));
        salvar(categoriaDTO);
    }

    /**
     * Metodo para deletar uma categoria.
     *
     * @param id Chave identificadora da categoria que deve ser deletada.
     * @since 1.0
     */
    public void deletar(Long id) {
        Categoria categoriaModel = categoriaRepository.findById(id)
                .orElseThrow(() -> new DadoNaoEncontradoException("Categoria não encontrada." +
                        " Tipo: " + Categoria.class.getName()));

        if (!categoriaModel.getProdutos().isEmpty())
            throw new DadoRelacionadoException("A categoria [ " + categoriaModel.getNome() + " ] não pode ser " +
                    "excluída pois está relacionada com um ou mais produtos");

        categoriaRepository.deleteById(id);
    }

}
