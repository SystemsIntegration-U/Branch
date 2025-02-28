package system.integration.branchInventory.listenner;

import org.springframework.stereotype.Service;

import system.integration.branchInventory.domain.model.Reservation;
import system.integration.branchInventory.dto.StockAlertEventDTO;
import system.integration.branchInventory.service.BatchService;
import system.integration.branchInventory.service.ReservationService;
import system.integration.branchInventory.service.StockEventPublisher;

@Service

public class ReservationListener {
    private final BatchService batchService;
    private final ReservationService reservationService;
    private final StockEventPublisher stockEventPublisher;
    
    public ReservationListener(BatchService batchService, ReservationService reservationService, StockEventPublisher stockEventPublisher) {
        this.batchService = batchService;
        this.reservationService = reservationService;
        this.stockEventPublisher = stockEventPublisher;
    }
    
    public void handleReservationRequest(StockAlertEventDTO request) {
        try {
            if (batchService.getAvailableStock(request.getMedicineId()) >= request.getRequiredQuantity()) {
                Reservation reservation = reservationService.createReservation(request.getMedicineId(), request.getRequiredQuantity());
                batchService.reserveStock(reservation);
                stockEventPublisher.sendEvent("reservation_confirmations_queue", reservation.getId().toString());
            } else {
                stockEventPublisher.sendEvent("stock_insufficient_queue", request.getMedicineId().toString());
            }
        } catch (Exception e) {
            stockEventPublisher.sendEvent("reservation_failed_queue", "Reservation failed for medicine: " + request.getMedicineId());
        }
    }
}    
