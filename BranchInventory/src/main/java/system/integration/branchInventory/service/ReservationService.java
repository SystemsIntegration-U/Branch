package system.integration.branchInventory.service;

import org.springframework.stereotype.Service;
import system.integration.branchInventory.domain.model.Reservation;
import system.integration.branchInventory.repository.IReservationRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {
    private final IReservationRepository reservationRepository;

    public ReservationService(IReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(UUID id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(UUID id) {
        reservationRepository.deleteById(id);
    }
}
