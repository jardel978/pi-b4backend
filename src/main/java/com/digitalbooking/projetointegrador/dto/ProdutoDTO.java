package com.digitalbooking.projetointegrador.dto;

import lombok.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

   
    private Long id;

    @NotNull(message = "O nome do produto não pode ser nulo")
    private String nome;

    @Size(max = 3000, message = "A descrição informada excede a quantidade máxima de 3000 caracteres")
    private String descricao;

    @DecimalMin(value = "0.0", message = "O valor da nota de avaliação não pode ser menor que 0")
    @DecimalMax(value = "10.0", message = "O valor da nota de avaliação não pode ser maior que 10.0")
    private Double notaDeAvaliacao;

    @DecimalMin(value = "0.0", message = "O valor de reserva por pessoa não pode ser menor que 0")
    private Double valorPorPessoa;

    private int qtdAvaliacoes;

    @Size(max = 5, message = "A quantidade máxima de 5 imagens para esse produto foi excedida")
    private Set<ImagemDTO> imagens;

    @NotNull(message = "O endereço deve ser informado")
    private String endereco;

    @NotNull(message = "A cidade é um campo obrigatório")
    private CidadeDTO cidade;

    @NotNull(message = "A latitude deve ser informada")
    private Double latitude;

    @NotNull(message = "A longitude deve ser informada")
    private Double longitude;

    @NotNull(message = "A categoria do produto deve ser informada")
    private CategoriaDTO categoria;

    private Set<CaracteristicaDTO> caracteristicas;

    private Set<ReservaMixProdutoDTO> reservas;

    @NotNull(message = "A quantidade limite diária de pessoas que o camping comporta deve ser informada")
    private Integer limitePessoasPorDia;

    private List<LocalDate> datasIndisponiveis;

}
