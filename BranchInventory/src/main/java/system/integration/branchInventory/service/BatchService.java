package system.integration.branchInventory.service;

import org.springframework.stereotype.Service;
import system.integration.branchInventory.domain.model.Batch;
import system.integration.branchInventory.domain.model.Medicine;
import system.integration.branchInventory.dto.BatchDTO;
import system.integration.branchInventory.mappers.BatchMapper;
import system.integration.branchInventory.repository.IBatchRepository;
import system.integration.branchInventory.repository.IMedicineRepository;

import java.util.UUID;

@Service
public class BatchService extends GenericService<Batch, UUID>  {

    private final IMedicineRepository medicineRepository;
    private final BatchMapper batchMapper;

    public BatchService(IBatchRepository batchRepository, IMedicineRepository medicineRepository, BatchMapper batchMapper) {
        super(batchRepository);
        this.medicineRepository = medicineRepository;
        this.batchMapper = batchMapper;
    }

    public BatchDTO saveBatch(BatchDTO batchDTO) {
        Medicine medicine = medicineRepository.findById(batchDTO.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
    
        Batch batch = batchMapper.toEntity(batchDTO, medicine);
        medicine.setStock(medicine.getStock() + batch.getStock());
        medicineRepository.save(medicine);
    
        Batch savedBatch = save(batch);
    
        return batchMapper.toDTO(savedBatch); 
    }
    
}