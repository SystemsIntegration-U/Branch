package systems.integration.branch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.integration.branch.domain.Medication;

import java.time.LocalDate;
import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    // Medicamentos con stock mayor que 0
    List<Medication> findByStockGreaterThan(int minStock);

    // Medicamentos cuya fecha de caducidad es posterior a la fecha indicada
    List<Medication> findByExpiryDateAfter(LocalDate date);

    // Buscar medicamento por nombre, que no esté vencido y tenga stock
    List<Medication> findByNameAndExpiryDateAfterAndStockGreaterThan(String name, LocalDate date, int stock);
}
