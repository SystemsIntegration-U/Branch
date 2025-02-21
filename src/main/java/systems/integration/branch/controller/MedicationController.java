package systems.integration.branch.controller;

import org.springframework.web.bind.annotation.*;
import systems.integration.branch.domain.Medication;
import systems.integration.branch.service.MedicationService;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public List<Medication> getAllMedications() {
        return medicationService.getAllMedications();
    }

    @GetMapping("/{id}")
    public Medication getMedicationById(@PathVariable Long id) {
        return medicationService.getMedicationById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found"));
    }

    @PostMapping
    public Medication createMedication(@RequestBody Medication medication) {
        return medicationService.createMedication(medication);
    }

    @PutMapping("/{id}")
    public Medication updateMedication(@PathVariable Long id, @RequestBody Medication medication) {
        return medicationService.updateMedication(id, medication);
    }

    @DeleteMapping("/{id}")
    public void deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
    }

    // Endpoint para medicamentos con stock > 0
    @GetMapping("/available")
    public List<Medication> getAvailableMedications() {
        return medicationService.getAvailableMedications();
    }

    // Endpoint para medicamentos no vencidos
    @GetMapping("/nonexpired")
    public List<Medication> getNonExpiredMedications() {
        return medicationService.getNonExpiredMedications();
    }

    // Endpoint para buscar medicamento por nombre (válido: no vencido y con stock)
    @GetMapping("/search")
    public List<Medication> getValidMedicationByName(@RequestParam String name) {
        return medicationService.getValidMedicationByName(name);
    }
}