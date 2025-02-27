package system.integration.branchInventory.service;

import system.integration.branchInventory.domain.model.Medicine;
import system.integration.branchInventory.repository.IMedicineRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class MedicineService {
    private final IMedicineRepository medicineRepository;

    public MedicineService(IMedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    public Medicine getMedicineById(UUID id) {
        return medicineRepository.findById(id).orElse(null);
    }

    public Medicine saveMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    public void deleteMedicine(UUID id) {
        medicineRepository.deleteById(id);
    }
}
