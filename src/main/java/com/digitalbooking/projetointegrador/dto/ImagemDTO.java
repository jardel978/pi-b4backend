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
@NoArgsConstructor
@AllArgsConstructor
public class ImagemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String titulo;

    @NotNull(message = "A url da imagem é obrigatória")
    private String url;

    private boolean ehImagemCapa;

}
