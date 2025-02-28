package system.integration.branchInventory.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import system.integration.branchInventory.domain.model.ReservationDetail;
import system.integration.branchInventory.repository.IReservationDetailRepository;

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

    public List<ReservationDetail> getReservationDetailsByReservationId(UUID reservationId) {
        return reservationDetailRepository.findAll().stream()
                .filter(detail -> detail.getReservation().getId().equals(reservationId))
                .collect(Collectors.toList());
    }

    public ReservationDetail saveReservationDetail(ReservationDetail reservationDetail) {
        return reservationDetailRepository.save(reservationDetail);
    }

    public void deleteReservationDetail(UUID id) {
        reservationDetailRepository.deleteById(id);
    }
}
