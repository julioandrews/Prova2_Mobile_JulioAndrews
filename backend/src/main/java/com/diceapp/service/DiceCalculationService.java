package com.diceapp.service;

import com.diceapp.model.DiceCalculation;
import com.diceapp.repository.DiceCalculationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiceCalculationService {

    private final DiceCalculationRepository repository;

    public DiceCalculationService(DiceCalculationRepository repository) {
        this.repository = repository;
    }

    // CRUD Básico
    public List<DiceCalculation> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public DiceCalculation findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public DiceCalculation save(DiceCalculation calculo) {
        calculo.setProbability(calcularProbabilidade(calculo));
        return repository.save(calculo);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public DiceCalculation update(Long id, DiceCalculation calculoAtualizado) {
        return repository.findById(id)
                .map(calculo -> {
                    calculo.setNumDice(calculoAtualizado.getNumDice());
                    calculo.setNumSides(calculoAtualizado.getNumSides());
                    calculo.setCalculationType(calculoAtualizado.getCalculationType());
                    calculo.setMinWidth(calculoAtualizado.getMinWidth());
                    calculo.setMinHeight(calculoAtualizado.getMinHeight());
                    calculo.setProbability(calcularProbabilidade(calculoAtualizado));
                    return repository.save(calculo);
                })
                .orElse(null);
    }

    // Buscas específicas
    public List<DiceCalculation> findByType(String tipo) {
        return repository.findByCalculationType(tipo);
    }

    public List<DiceCalculation> findAllOrderByProbability() {
        return repository.findAllByOrderByProbabilityDesc();
    }

    // Calculos de probabilidade
    private Double calcularProbabilidade(DiceCalculation calc) {
        try {
            switch(calc.getCalculationType()) {
                case "width":
                    return probMinWidth(calc.getNumDice(), calc.getNumSides(),
                            calc.getMinWidth() != null ? calc.getMinWidth() : 2);
                case "height":
                    return probMinHeight(calc.getNumDice(), calc.getNumSides(),
                            calc.getMinHeight() != null ? calc.getMinHeight() : 1);
                case "both":
                    return probMinWidthAndHeight(calc.getNumDice(), calc.getNumSides(),
                            calc.getMinWidth() != null ? calc.getMinWidth() : 2,
                            calc.getMinHeight() != null ? calc.getMinHeight() : 1);
                default:
                    return 0.0;
            }
        } catch (Exception e) {
            return 0.0;
        }
    }

    // === FUNÇÕES DE CÁLCULO ===

    // permutation
    private long perm(int a, int b) {
        if (b < 0 || b > a) return 0;
        long result = 1;
        for (int i = 0; i < b; i++) {
            result *= (a - i);
        }
        return result;
    }

    // combination
    private long comb(int a, int b) {
        return perm(a, b) / perm(b, b);
    }

    // probability of at least 1 match of at least 'h' height
    private double probMinHeight(int n, int d, int h) {
        double probTotal = 0;
        int validH = d - h + 1;

        for (int m = 2; m <= n; m++) {
            double probX = (comb(n, m) * (Math.pow(validH, m) - perm(validH, m)) * Math.pow(h-1, n-m)) / Math.pow(d, n);
            probTotal += probX;
        }
        return probTotal;
    }

    // probability of at least 1 match of at least 'w' width
    private long probMinWidthAux(int n, int d, int w) {
        if (n <= 0) {
            return 1;
        }
        if (d == 0) {
            return 0;
        }

        long total = 0;
        int maxDice = (w-1 < n) ? w-1 : n;

        for (int k = 0; k <= maxDice; k++) {
            total += comb(n, k) * probMinWidthAux(n - k, d - 1, w);
        }
        return total;
    }

    private double probMinWidth(int n, int d, int w) {
        if (w > n || d == 0) {
            return 0.0;
        }
        long valid = probMinWidthAux(n, d, w);
        return 1 - ((double) valid / Math.pow(d, n));
    }

    // probability of at least 1 match of at least 'w' width AND 'h' height
    private long probMinWidthAndHeightAux(int n, int d, int w, int h) {
        if (n == 0) {
            return 1;
        }
        if (d == 0) {
            return 0;
        }

        long total = 0;
        int highFaces = d - h + 1;

        if (highFaces > 0) {
            int maxDiceHigh = Math.min(w - 1, n);
            for (int k = 0; k <= maxDiceHigh; k++) {
                total += comb(n, k) * probMinWidthAndHeightAux(n - k, d - 1, w, h);
            }
        } else {
            for (int k = 0; k <= n; k++) {
                total += comb(n, k) * probMinWidthAndHeightAux(n - k, d - 1, w, h);
            }
        }
        return total;
    }

    private double probMinWidthAndHeight(int n, int d, int w, int h) {
        if (w > n || d == 0 || h > d) {
            return 0.0;
        }
        long valid = probMinWidthAndHeightAux(n, d, w, h);
        return 1 - ((double) valid / Math.pow(d, n));
    }
}