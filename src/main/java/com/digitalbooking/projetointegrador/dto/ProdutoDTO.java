package com.digitalbooking.projetointegrador.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "O nome do produto não pode ser nulo")
    private String nome;

    @Size(max = 3000, message = "A descrição informada excede a quantidade máxima de 3000 caracteres")
    private String descricao;

    @Size(max = 5, message = "A quantidade máxima de 5 imagens para esse produto foi excedida")
    private Set<ImagemDTO> imagens;

    @NotNull(message = "A cidade é um campo obrigatório")
    private CidadeDTO cidade;

    @NotNull(message = "A categoria do produto deve ser informada")
    private CategoriaDTO categoria;

    private Set<CaracteristicaDTO> caracteristicas;

    private Set<ReservaDTO> reservas;

}
