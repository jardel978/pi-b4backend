package com.digitalbooking.projetointegrador.dto;


import lombok.*;

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
