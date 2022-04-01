package com.digitalbooking.projetointegrador.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NomeFuncao {

    ADMIN("admin", 1),
    USER("user", 2);

    private final String nome;
    private final int codigo;

}
