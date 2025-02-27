package system.integration.branchInventory.repository;

import system.integration.branchInventory.domain.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface IBatchRepository extends JpaRepository<Batch, UUID> {
}
