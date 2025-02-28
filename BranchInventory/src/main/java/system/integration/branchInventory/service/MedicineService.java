package system.integration.branchInventory.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import java.util.List;
import system.integration.branchInventory.RabbitMQEventPublisher;
import system.integration.branchInventory.domain.model.Medicine;
import system.integration.branchInventory.repository.IMedicineRepository;

@Service
public class MedicineService extends GenericService<Medicine, UUID> {
    private final RabbitMQEventPublisher eventPublisher;
    private final BatchService batchService;
    private final IMedicineRepository medicineRepository;

    public MedicineService(IMedicineRepository medicineRepository,
                           RabbitMQEventPublisher eventPublisher,
                           BatchService batchService) {
        super(medicineRepository);

        this.eventPublisher = eventPublisher;
        this.batchService = batchService;
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

    public void notifyGeoService(UUID medicineId, int neededQuantity) {
        String request = String.format("Find alternative: Medicine ID %s needs %d units.", medicineId, neededQuantity);
        eventPublisher.sendEvent("geo_location_queue", request);
    }

    public Medicine reduceStock(UUID id, int quantity) {
        Medicine medicine = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Medicine not found"));

        int availableStock = batchService.getAvailableStock(id);

        if (availableStock >= quantity) {
            medicine.setStock(medicine.getStock() - quantity);
            repository.save(medicine);
            return medicine;
        } else {
            notifyGeoService(id, quantity - availableStock);
            throw new RuntimeException("Insufficient stock. Searching for another branch.");
        }
    }
}

