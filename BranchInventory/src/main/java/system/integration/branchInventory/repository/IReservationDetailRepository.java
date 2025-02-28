package system.integration.branchInventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import system.integration.branchInventory.domain.model.ReservationDetail;

import java.util.UUID;

public interface IReservationDetailRepository extends JpaRepository<ReservationDetail, UUID> {
}