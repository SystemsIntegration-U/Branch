package system.integration.branchInventory.mappers;

import org.springframework.stereotype.Component;
import system.integration.branchInventory.domain.model.Batch;
import system.integration.branchInventory.domain.model.Medicine;
import system.integration.branchInventory.dto.BatchDTO;

import java.util.UUID;

@Component
public class BatchMapper {

    public BatchDTO toDTO(Batch batch) {
        return new BatchDTO(
                batch.getMedicine().getId(), 
                batch.getExpiryDate(),
                batch.getStock()
        );
    }

    public Batch toEntity(BatchDTO dto, Medicine medicine) {
        return new Batch(
                UUID.randomUUID(),
                medicine, 
                dto.getExpiryDate(),
                dto.getStock()
        );
    }
}
