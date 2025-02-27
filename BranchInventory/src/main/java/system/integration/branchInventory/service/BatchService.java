package system.integration.branchInventory.service;

import org.springframework.stereotype.Service;
import system.integration.branchInventory.domain.model.Batch;
import system.integration.branchInventory.domain.model.Medicine;
import system.integration.branchInventory.repository.IBatchRepository;
import system.integration.branchInventory.repository.IMedicineRepository;

import java.util.List;
import java.util.UUID;

@Service
public class BatchService {
    private final IBatchRepository batchRepository;
    private final IMedicineRepository medicineRepository;

    public BatchService(IBatchRepository batchRepository, IMedicineRepository medicineRepository) {
        this.batchRepository = batchRepository;
        this.medicineRepository = medicineRepository;
    }

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public Batch getBatchById(UUID id) {
        return batchRepository.findById(id).orElse(null);
    }

    public Batch saveBatch(Batch batch) {
        Medicine medicine = medicineRepository.findById(batch.getMedicine().getId()).orElseThrow(
                () -> new RuntimeException("Medicine not found")
        );
        medicine.setStock(medicine.getStock() + batch.getStock());
        medicineRepository.save(medicine);
        return batchRepository.save(batch);
    }

    public void deleteBatch(UUID id) {
        batchRepository.deleteById(id);
    }
}