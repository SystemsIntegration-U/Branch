package systems.integration.branch.service;

import org.springframework.stereotype.Service;
import systems.integration.branch.domain.Medication;
import systems.integration.branch.repository.MedicationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    public Optional<Medication> getMedicationById(Long id) {
        return medicationRepository.findById(id);
    }

    public Medication createMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication updateMedication(Long id, Medication updatedMedication) {
        Optional<Medication> optionalMedication = medicationRepository.findById(id);
        if (optionalMedication.isPresent()) {
            Medication medication = optionalMedication.get();
            medication.setName(updatedMedication.getName());
            medication.setDescription(updatedMedication.getDescription());
            medication.setExpiryDate(updatedMedication.getExpiryDate());
            medication.setStock(updatedMedication.getStock());
            medication.setPrice(updatedMedication.getPrice());
            return medicationRepository.save(medication);
        }
        throw new RuntimeException("Medication not found");
    }

    public void deleteMedication(Long id) {
        medicationRepository.deleteById(id);
    }

    public List<Medication> getAvailableMedications() {
        return medicationRepository.findByStockGreaterThan(0);
    }

    public List<Medication> getNonExpiredMedications() {
        return medicationRepository.findByExpiryDateAfter(LocalDate.now());
    }

    public List<Medication> getValidMedicationByName(String name) {
        return medicationRepository.findByNameAndExpiryDateAfterAndStockGreaterThan(name, LocalDate.now(), 0);
    }
}
