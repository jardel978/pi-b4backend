package com.digitalbooking.projetointegrador.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO extends UsuarioDTO {

    private Set<ReservaDTO> reservas;

    private String endereco;

}
