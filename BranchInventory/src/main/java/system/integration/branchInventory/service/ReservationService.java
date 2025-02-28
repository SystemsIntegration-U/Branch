package system.integration.branchInventory.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import system.integration.branchInventory.domain.model.Batch;
import system.integration.branchInventory.domain.model.Reservation;
import system.integration.branchInventory.domain.model.ReservationDetail;
import system.integration.branchInventory.repository.IBatchRepository;
import system.integration.branchInventory.repository.IReservationDetailRepository;
import system.integration.branchInventory.repository.IReservationRepository;

@Service
public class ReservationService {
    private final IReservationRepository reservationRepository;
    private final IReservationDetailRepository reservationDetailRepository;
    private final IBatchRepository batchRepository;

    public ReservationService(IReservationRepository reservationRepository,
                              IReservationDetailRepository reservationDetailRepository,
                              IBatchRepository batchRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationDetailRepository = reservationDetailRepository;
        this.batchRepository = batchRepository;
    }

    public Reservation createReservation(UUID medicineId, int requiredQuantity) {
        Reservation reservation = new Reservation();
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setExpiryDate(LocalDateTime.now().plusDays(3));
        reservation.setStatus("PENDING");
        reservation = reservationRepository.save(reservation);

        List<Batch> availableBatches = batchRepository.findAll().stream()
                .filter(batch -> batch.getMedicine().getId().equals(medicineId) && batch.getStock() > 0)
                .sorted((b1, b2) -> b1.getExpiryDate().compareTo(b2.getExpiryDate()))
                .collect(Collectors.toList());

        int remainingQuantity = requiredQuantity;

        for (Batch batch : availableBatches) {
            if (remainingQuantity <= 0) break;
            int reservedQty = Math.min(remainingQuantity, batch.getStock());

            ReservationDetail detail = new ReservationDetail();
            detail.setReservation(reservation);
            detail.setBatch(batch);
            detail.setQuantity(reservedQty);
            reservationDetailRepository.save(detail);

            remainingQuantity -= reservedQty;
        }

        return reservation;
    }

    public void confirmReservation(UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStatus("CONFIRMED");
        reservationRepository.save(reservation);
    }
}
