package system.integration.branchInventory.repository;

import system.integration.branchInventory.domain.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface IClientRepository extends JpaRepository<Client, UUID> {
}