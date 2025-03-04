package system.integration.branchInventory.Presentation.Producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import system.integration.branchInventory.domain.model.Medicine;
import system.integration.branchInventory.dto.LocationDTO;
import system.integration.branchInventory.dto.MedicineDTO;
import system.integration.branchInventory.dto.RequiredMedicineDTO;

@Component
@Slf4j
public class RequiredMedicineProducer {

    private final RabbitTemplate rabbitTemplate;
    public static final String MEDICINE_SEARCH_EXCHANGE = "medicine.search.exchange";
    public static final String MEDICINE_SEARCH_QUEUE = "medicine.search.queue";
    public static final String FANOUT_KEY = "";



    public RequiredMedicineProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMedicineRequest(Medicine medicine, LocationDTO location) {
        double range = 500 + (Math.random() * 500);

        RequiredMedicineDTO requiredMedicineDTO = new RequiredMedicineDTO(new MedicineDTO(medicine.getId(), medicine.getName(), medicine.getStock()),location,range);

        try {
            rabbitTemplate.convertAndSend(
                    MEDICINE_SEARCH_EXCHANGE,
                    FANOUT_KEY,
                    requiredMedicineDTO
            );
            log.info("sendMedicineRequest: (medicineId: {}, range: {})",
                    requiredMedicineDTO.getProductDetails().getId(), requiredMedicineDTO.getRange());
        } catch (Exception exception) {
            log.warn(exception.getMessage());
        }
    }
}
