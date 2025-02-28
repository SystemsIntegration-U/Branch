package system.integration.branchInventory.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import system.integration.branchInventory.domain.model.Reservation;
import system.integration.branchInventory.dto.StockAlertEventDTO;

import java.util.UUID;

@Service
public class ReservationEventService {
    private final ReservationService reservationService;
    private final MedicineService medicineService;
    private final RabbitTemplate rabbitTemplate;

    public ReservationEventService(ReservationService reservationService,
                                   MedicineService medicineService,
                                   RabbitTemplate rabbitTemplate) {
        this.reservationService = reservationService;
        this.medicineService = medicineService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "reservation_request_queue")
    public void handleReservationRequest(StockAlertEventDTO request) {
        System.out.println("Received reservation request for medicine: " + request.getMedicineId());
        
        Reservation reservation = reservationService.createReservation(request.getMedicineId(), request.getRequiredQuantity());

        if (reservation.getStatus().equals("PENDING")) {
            ReservationConfirmationEvent confirmationEvent = new ReservationConfirmationEvent(
                reservation.getId(), request.getMedicineId(), request.getRequiredQuantity()
            );
            rabbitTemplate.convertAndSend("reservation_confirmation_queue", confirmationEvent);
        }
    }

    @RabbitListener(queues = "reservation_confirmation_queue")
    public void handleReservationConfirmation(ReservationConfirmationEvent confirmationEvent) {
        System.out.println("Received reservation confirmation for reservation ID: " + confirmationEvent.getReservationId());
        
        reservationService.confirmReservation(confirmationEvent.getReservationId());

        medicineService.increaseStock(confirmationEvent.getMedicineId(), confirmationEvent.getQuantity());
    }
}

