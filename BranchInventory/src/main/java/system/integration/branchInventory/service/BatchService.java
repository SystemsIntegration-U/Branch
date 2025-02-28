package system.integration.branchInventory.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import system.integration.branchInventory.domain.model.Batch;
import system.integration.branchInventory.domain.model.Medicine;
import system.integration.branchInventory.domain.model.Reservation;
import system.integration.branchInventory.domain.model.ReservationDetail;
import system.integration.branchInventory.dto.BatchDTO;
import system.integration.branchInventory.mappers.BatchMapper;
import system.integration.branchInventory.repository.IBatchRepository;
import system.integration.branchInventory.repository.IMedicineRepository;
import system.integration.branchInventory.repository.IReservationDetailRepository;

@Service
public class BatchService extends GenericService<Batch, UUID>  {

    private final IMedicineRepository medicineRepository;
    private final IReservationDetailRepository reservationDetailRepository;
    private final BatchMapper batchMapper;

    public BatchService(IBatchRepository batchRepository, 
                        IMedicineRepository medicineRepository, 
                        IReservationDetailRepository reservationDetailRepository,
                        BatchMapper batchMapper) {
        super(batchRepository);
        this.medicineRepository = medicineRepository;
        this.reservationDetailRepository = reservationDetailRepository;
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


    public List<Batch> getAvailableBatches() {
        return repository.findAll().stream()
            .filter(batch -> batch.getExpiryDate().isAfter(LocalDate.now()))
            .collect(Collectors.toList());
    }


    public int getAvailableStock(UUID medicineId) {
        int totalStock = repository.findAll().stream()
            .filter(batch -> batch.getMedicine().getId().equals(medicineId) && batch.getExpiryDate().isAfter(LocalDate.now()))
            .mapToInt(Batch::getStock)
            .sum();

        int reservedStock = reservationDetailRepository.findAll().stream()
            .filter(reservation -> reservation.getBatch().getMedicine().getId().equals(medicineId))
            .mapToInt(ReservationDetail::getQuantity)
            .sum();

        return totalStock - reservedStock;
    }
    
    public void reserveStock(Reservation reservation) {
        for (ReservationDetail detail : reservation.getReservationDetails()) {
            Batch batch = detail.getBatch();
            if (batch.getStock() >= detail.getQuantity()) {
                batch.setStock(batch.getStock() - detail.getQuantity());
                repository.save(batch);
            } else {
                throw new RuntimeException("Not enough stock in batch: " + batch.getId());
            }
        }
    }
}
