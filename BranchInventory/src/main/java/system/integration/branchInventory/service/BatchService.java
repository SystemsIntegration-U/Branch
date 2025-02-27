package system.integration.branchInventory.service;

import org.springframework.stereotype.Service;
import system.integration.branchInventory.domain.model.Batch;
import system.integration.branchInventory.repository.IBatchRepository;

import java.util.List;
import java.util.UUID;

@Service
public class BatchService {
    private final IBatchRepository batchRepository;

    public BatchService(IBatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public Batch getBatchById(UUID id) {
        return batchRepository.findById(id).orElse(null);
    }

    public Batch saveBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    public void deleteBatch(UUID id) {
        batchRepository.deleteById(id);
    }
}