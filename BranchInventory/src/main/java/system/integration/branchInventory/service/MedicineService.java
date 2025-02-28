package system.integration.branchInventory.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import system.integration.branchInventory.domain.model.Medicine;
import system.integration.branchInventory.repository.IMedicineRepository;

@Service
public class MedicineService extends GenericService<Medicine, UUID> {
    private final StockEventPublisher stockEventPublisher;

    public MedicineService(IMedicineRepository medicineRepository, StockEventPublisher stockEventPublisher) {
        super(medicineRepository);
        this.stockEventPublisher = stockEventPublisher;
    }

    public Medicine reduceStock(UUID id, int quantity) {
        Medicine medicine = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        if (medicine.getStock() >= quantity) {
            medicine.setStock(medicine.getStock() - quantity);
            Medicine updatedMedicine = repository.save(medicine);
            stockEventPublisher.checkAndPublishStockEvent(updatedMedicine);

            return updatedMedicine;
        } else {
            throw new RuntimeException("Insufficient stock. Searching for another branch.");
        }
    }
}
