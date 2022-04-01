package com.digitalbooking.projetointegrador.dto;


import lombok.*;

import javax.validation.constraints.NotNull;
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
@AllArgsConstructor
@NoArgsConstructor
public class CidadeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "O nome da cidade é obrigatório")
    private String nome;

    @NotNull(message = "O país em que a cidade se encontra é obrigatório")
    private String pais;

}
