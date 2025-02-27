package system.integration.branchInventory.service;

import org.springframework.stereotype.Service;
import system.integration.branchInventory.domain.model.ReservationDetail;
import system.integration.branchInventory.repository.IReservationDetailRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ReservationDetailService {
    private final IReservationDetailRepository reservationDetailRepository;

    public ReservationDetailService(IReservationDetailRepository reservationDetailRepository) {
        this.reservationDetailRepository = reservationDetailRepository;
    }

    public List<ReservationDetail> getAllReservationDetails() {
        return reservationDetailRepository.findAll();
    }

    public ReservationDetail getReservationDetailById(UUID id) {
        return reservationDetailRepository.findById(id).orElse(null);
    }

    public ReservationDetail saveReservationDetail(ReservationDetail reservationDetail) {
        return reservationDetailRepository.save(reservationDetail);
    }

    public void deleteReservationDetail(UUID id) {
        reservationDetailRepository.deleteById(id);
    }
}