package system.integration.branchInventory;

import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;


@Component
public class RabbitMQEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Envía un evento genérico a RabbitMQ.
     */
    public void sendEvent(String queueName, Object eventData) {
        try {
            String message = objectMapper.writeValueAsString(eventData);
            rabbitTemplate.convertAndSend(queueName, message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting event data to JSON", e);
        }
    }


    public void notifyGeoService(UUID medicineId, int neededQuantity) {
        Map<String, Object> event = new HashMap<>();
        event.put("medicineId", medicineId.toString());
        event.put("neededQuantity", neededQuantity);
        event.put("message", String.format("Find alternative: Medicine ID %s needs %d units.", medicineId, neededQuantity));

        sendEvent("geo_location_queue", event);
    }
}
