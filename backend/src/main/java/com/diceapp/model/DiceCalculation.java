// backend/src/main/java/com/diceapp/model/DiceCalculation.java
package com.diceapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dice_calculations")
public class DiceCalculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Número de dados é obrigatório")
    @Min(value = 1, message = "Mínimo 1 dado")
    @Max(value = 10, message = "Máximo 10 dados")
    private Integer numDice;

    @NotNull(message = "Número de lados é obrigatório")
    @Min(value = 2, message = "Mínimo 2 lados")
    @Max(value = 10, message = "Máximo 10 lados")
    private Integer numSides;

    @NotBlank(message = "Tipo de cálculo é obrigatório")
    private String calculationType; // "width", "height", "both"

    private Integer minWidth;
    private Integer minHeight;

    private Double probability;

    private LocalDateTime createdAt;

    public DiceCalculation() {
        this.createdAt = LocalDateTime.now();
    }

    public DiceCalculation(Integer numDice, Integer numSides, String calculationType,
                           Integer minWidth, Integer minHeight) {
        this();
        this.numDice = numDice;
        this.numSides = numSides;
        this.calculationType = calculationType;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    // GETTERS E SETTERS (todos manualmente)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getNumDice() { return numDice; }
    public void setNumDice(Integer numDice) { this.numDice = numDice; }

    public Integer getNumSides() { return numSides; }
    public void setNumSides(Integer numSides) { this.numSides = numSides; }

    public String getCalculationType() { return calculationType; }
    public void setCalculationType(String calculationType) { this.calculationType = calculationType; }

    public Integer getMinWidth() { return minWidth; }
    public void setMinWidth(Integer minWidth) { this.minWidth = minWidth; }

    public Integer getMinHeight() { return minHeight; }
    public void setMinHeight(Integer minHeight) { this.minHeight = minHeight; }

    public Double getProbability() { return probability; }
    public void setProbability(Double probability) { this.probability = probability; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "DiceCalculation{" +
                "id=" + id +
                ", numDice=" + numDice +
                ", numSides=" + numSides +
                ", calculationType='" + calculationType + '\'' +
                ", minWidth=" + minWidth +
                ", minHeight=" + minHeight +
                ", probability=" + probability +
                ", createdAt=" + createdAt +
                '}';
    }
}