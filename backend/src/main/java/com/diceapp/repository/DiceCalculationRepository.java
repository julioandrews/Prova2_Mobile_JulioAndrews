// backend/src/main/java/com/diceapp/repository/DiceCalculationRepository.java
package com.diceapp.repository;

import com.diceapp.model.DiceCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiceCalculationRepository extends JpaRepository<DiceCalculation, Long> {
    List<DiceCalculation> findAllByOrderByCreatedAtDesc();
    List<DiceCalculation> findByCalculationType(String calculationType);
    List<DiceCalculation> findAllByOrderByProbabilityDesc();
}