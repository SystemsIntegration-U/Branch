package system.integration.branchInventory.repository;

import system.integration.branchInventory.domain.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface IMedicineRepository extends JpaRepository<Medicine, UUID> {
}
