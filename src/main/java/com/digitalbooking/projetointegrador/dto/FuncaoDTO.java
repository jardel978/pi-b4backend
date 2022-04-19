package com.digitalbooking.projetointegrador.dto;


import com.digitalbooking.projetointegrador.model.enums.NomeFuncao;
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
public class FuncaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "O nome da função é obrigatório")
    private NomeFuncao nomeFuncaoEnum;

//    private Set<UsuarioDTO> usuarios;
}
