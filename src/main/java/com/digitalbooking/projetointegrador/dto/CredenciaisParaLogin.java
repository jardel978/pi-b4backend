package com.digitalbooking.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CredenciaisParaLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String senha;

    @Override
    public String toString() {
        return "DadosParaLogin{" +
                "email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}
