package system.integration.branchInventory.Controller;

import org.springframework.web.bind.annotation.*;
import system.integration.branchInventory.domain.model.ReservationDetail;
import system.integration.branchInventory.service.ReservationDetailService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reservation-detail")
public class ReservationDetailController {
    private final ReservationDetailService reservationDetailService;

    public ReservationDetailController(ReservationDetailService reservationDetailService) {
        this.reservationDetailService = reservationDetailService;
    }

    @GetMapping
    public List<ReservationDetail> getAllReservationDetails() {
        return reservationDetailService.getAllReservationDetails();
    }

    @GetMapping("/{id}")
    public ReservationDetail getReservationDetailById(@PathVariable UUID id) {
        return reservationDetailService.getReservationDetailById(id);
    }

    @PostMapping
    public ReservationDetail createReservationDetail(@RequestBody ReservationDetail reservationDetail) {
        return reservationDetailService.saveReservationDetail(reservationDetail);
    }

    @DeleteMapping("/{id}")
    public void deleteReservationDetail(@PathVariable UUID id) {
        reservationDetailService.deleteReservationDetail(id);
    }
}
