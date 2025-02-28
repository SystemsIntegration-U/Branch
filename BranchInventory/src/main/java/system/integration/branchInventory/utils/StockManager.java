package system.integration.branchInventory.utils;

import org.springframework.stereotype.Service;
import system.integration.branchInventory.domain.model.Medicine;
import system.integration.branchInventory.repository.IMedicineRepository;

import java.util.UUID;

@Service
public class StockManager  {

    private final IMedicineRepository medicineRepository;

    public StockManager(IMedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public Medicine reduceStock(UUID id, int quantity) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        if (medicine.getStock() >= quantity) {
            medicine.setStock(medicine.getStock() - quantity);
            return medicineRepository.save(medicine);
        } else {
            throw new RuntimeException("Insufficient stock.");
        }
    }
}
