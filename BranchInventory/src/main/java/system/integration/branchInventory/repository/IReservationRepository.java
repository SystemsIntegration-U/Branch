package system.integration.branchInventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import system.integration.branchInventory.domain.model.Reservation;

import java.util.UUID;

public interface IReservationRepository extends JpaRepository<Reservation, UUID> {

}
