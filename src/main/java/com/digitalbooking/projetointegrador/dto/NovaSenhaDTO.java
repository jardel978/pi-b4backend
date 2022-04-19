package com.digitalbooking.projetointegrador.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * Classe para uso na atualizacao da senha de um usuario
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NovaSenhaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Email é um campo obrigatório")
    @Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", message =
            "Formato de email inválido. Exemplo: meuemail@gmail.com")
    private String email;

    @NotNull(message = "A senha atual deve ser informada")
    @Size(min = 7, max = 15, message = "A senha deve conter ao menos 7 caracteres e no máximo 15")
    private String senhaAtual;

    @NotNull(message = "A nova senha deve ser informada")
    @Size(min = 7, max = 15, message = "A senha deve conter ao menos 7 caracteres e no máximo 15")
    private String senhaNova;
}
