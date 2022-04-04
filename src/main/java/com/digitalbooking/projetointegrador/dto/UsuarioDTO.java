package com.digitalbooking.projetointegrador.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    @NotNull(message = "Nome é um campo obrigatório")
    private String nome;

    @NotNull(message = "Sobrenome é um campo obrigatório")
    private String sobrenome;

    @NotNull(message = "Email é um campo obrigatório")
    @Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", message =
            "Formato de email inválido. Exemplo: meuemail@gmail.com")
    private String email;

    @NotNull(message = "Senha é um campo obrigatório")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//ignora senha no retorno de um usuario
    private String senha;

    @NotNull(message = "Função é um campo obrigatório")
    private FuncaoDTO funcao;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean usuarioEstaValidado;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)//permite apenas retornar esse atributo
    private Date dataValidacaoRegistro;

    private Integer limitePessoasPorDia;

}
