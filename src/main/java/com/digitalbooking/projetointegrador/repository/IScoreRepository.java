package com.digitalbooking.projetointegrador.repository;

import com.digitalbooking.projetointegrador.model.Score;
import com.digitalbooking.projetointegrador.model.ScorePK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Classe de repository para <strong>Score</strong>.
 *
 * @version 1.0
 * @since 1.0
 */
public interface IScoreRepository extends JpaRepository<Score, ScorePK> {


}
