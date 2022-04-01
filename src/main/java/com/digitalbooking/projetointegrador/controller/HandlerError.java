package com.digitalbooking.projetointegrador.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe para personalizacao de respostas de erros.
 *
 * @version 1.0
 * @since 1.0
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HandlerError implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer status;
    private String mensagem;

    @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
    private Date data;

    private String path;

}
