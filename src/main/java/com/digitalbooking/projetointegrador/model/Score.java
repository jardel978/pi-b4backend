package com.digitalbooking.projetointegrador.model;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Classe para mapeamento da entidade Score
 *
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "tb_score_produto")
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ScorePK id = new ScorePK();

    private Double valor;

}
