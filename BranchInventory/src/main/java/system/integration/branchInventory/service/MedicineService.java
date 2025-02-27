package system.integration.branchInventory.service;

import system.integration.branchInventory.RabbitMQEventPublisher;
import system.integration.branchInventory.domain.model.Medicine;
import system.integration.branchInventory.repository.IMedicineRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class MedicineService {
    private final IMedicineRepository medicineRepository;
    private final RabbitMQEventPublisher eventPublisher;

    public MedicineService(IMedicineRepository medicineRepository, RabbitMQEventPublisher eventPublisher) {
        this.medicineRepository = medicineRepository;
        this.eventPublisher = eventPublisher;
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

    public Medicine reduceStock(UUID id, int quantity) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        if (medicine.getStock() >= quantity) {
            medicine.setStock(medicine.getStock() - quantity);
            return medicineRepository.save(medicine);
        } else {

            String message = String.format("Search petition : %s need %d units.",
                    medicine.getName(), quantity - medicine.getStock());
            eventPublisher.sendEvent("stock_request_queue", message);

            throw new RuntimeException("Insufficient stock. Searching for another branch.");
        }
    }
}
