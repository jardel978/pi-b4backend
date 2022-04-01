package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.ImagemDTO;
import com.digitalbooking.projetointegrador.model.Imagem;
import com.digitalbooking.projetointegrador.repository.IImagemRepository;
import com.digitalbooking.projetointegrador.service.exception.DadoNaoEncontradoException;
import com.digitalbooking.projetointegrador.service.exception.DadoRelacionadoException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Classe de service para <strong>Imagem</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@Service
public class ImagemService {

    @Autowired
    private IImagemRepository imagemRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Metodo que salva uma imagem no banco de dados.
     *
     * @param imagemDTO Imagem que deve ser persistida no banco de dados.
     * @since 1.0
     */
    public void salvar(ImagemDTO imagemDTO) {
        Imagem imagemModel = modelMapper.map(imagemDTO, Imagem.class);//mapeando/convertendo um objeto do
        //tipo ImagemDTO para Imagem
        imagemRepository.save(imagemModel);
    }

    /**
     * Metodo para busca de todas as imagens.
     *
     * @param pageable Interface abstrata para informacoes de paginacao.
     * @return Paginacao com imagens já convertidas de Imagem para ImagemDTO.
     * @since 1.0
     */
    public Page<ImagemDTO> buscarTodos(Pageable pageable) {
        Page<Imagem> listaImagens = imagemRepository.findAll(pageable);
        return listaImagens.map(imagemModel -> modelMapper.map(imagemModel, ImagemDTO.class));
    }

    /**
     * Metodo para atualizar uma Imagem.
     *
     * @param imagemDTO Imagem a ser atualizada.
     * @since 1.0
     */
    public void atualizar(ImagemDTO imagemDTO) {
        imagemRepository.findById(imagemDTO.getId())
                .orElseThrow(() -> new DadoNaoEncontradoException("Imagem não encontrada." +
                        " Tipo: " + Imagem.class.getName()));
        salvar(imagemDTO);
    }

    /**
     * Metodo para deletar uma Imagem.
     *
     * @param id Chave identificadora da Imagem que deve ser deletada.
     * @since 1.0
     */
    public void deletar(Long id) {
        Imagem imagemModel = imagemRepository.findById(id)
                .orElseThrow(() -> new DadoNaoEncontradoException("Imagem não encontrada." +
                        " Tipo: " + Imagem.class.getName()));

        if (imagemModel.getProduto() != null)
            throw new DadoRelacionadoException("Essa imagem não pode ser excluída pois está relacionada a um produto");

        imagemRepository.deleteById(id);
    }

}
