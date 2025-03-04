package system.integration.branchInventory.Presentation.Consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import system.integration.branchInventory.dto.BranchesWithMedicineDTO;
import system.integration.branchInventory.service.ReservationService;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class ReservationConsumer {

    private static final String BRANCHES_WITH_MEDICINE_QUEUE = "branches.with.medicine.queue";
    private final ReservationService reservationService;

    public ReservationConsumer(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @RabbitListener(queues = BRANCHES_WITH_MEDICINE_QUEUE, ackMode = "MANUAL")
    public void receiveMessage(BranchesWithMedicineDTO branchesWithMedicineDTO, Channel channel, Message message) {
        try {
            if (branchesWithMedicineDTO.getMedicineDTO() != null && branchesWithMedicineDTO.getRange() > 0) {
                UUID medicineId = branchesWithMedicineDTO.getMedicineDTO().getId();
                int requiredQuantity = branchesWithMedicineDTO.getMedicineDTO().getStock();

                reservationService.createReservation(medicineId, requiredQuantity);

                log.info("Received BranchesWithMedicineDTO and created reservation for medicine ID: {}"
                );
            }

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception exception) {
            log.warn(exception.getMessage());
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException e) {
                log.error("Failed to nack the message", e);
            }
        }
    }
}

