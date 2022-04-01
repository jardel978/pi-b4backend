package com.digitalbooking.projetointegrador.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Classe para transporte de dados entre as camadas de service, controller e entre cliente e servidor.
 *
 * @version 1.0
 * @since 1.0
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "O nome da categoria é obrigatório")
    private String nome;
    private String qualificacao;

    @Size(max = 3000, message = "A descrição informada excede a quantidade de 3000 caracteres")
    private String descricao;

    private String urlImagem;

}
