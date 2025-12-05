// backend/src/main/java/com/diceapp/controller/DiceCalculationController.java
package com.diceapp.controller;

import com.diceapp.model.DiceCalculation;
import com.diceapp.service.DiceCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calculations")
@CrossOrigin(origins = "*")
public class DiceCalculationController {

    private final DiceCalculationService service;

    // inj de dependencia
    public DiceCalculationController(DiceCalculationService service) {
        this.service = service;
    }

    // GET listar todos
    @GetMapping
    public List<DiceCalculation> getAllCalculations() {
        return service.findAll();
    }

    // GET buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<DiceCalculation> getCalculationById(@PathVariable Long id) {
        DiceCalculation calculation = service.findById(id);
        if (calculation != null) {
            return ResponseEntity.ok(calculation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST criar novo
    @PostMapping
    public DiceCalculation createCalculation(@RequestBody DiceCalculation calculation) {
        return service.save(calculation);
    }

    // PUT atualizar
    @PutMapping("/{id}")
    public ResponseEntity<DiceCalculation> updateCalculation(
            @PathVariable Long id,
            @RequestBody DiceCalculation calculation) {
        DiceCalculation updated = service.update(id, calculation);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE remover
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalculation(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // GET - Buscar por tipo
    @GetMapping("/search/type")
    public List<DiceCalculation> getCalculationsByType(@RequestParam String type) {
        return service.findByType(type);
    }

    // GET - Ordenar por probabilidade
    @GetMapping("/order/probability")
    public List<DiceCalculation> getCalculationsOrderByProbability() {
        return service.findAllOrderByProbability();
    }
}