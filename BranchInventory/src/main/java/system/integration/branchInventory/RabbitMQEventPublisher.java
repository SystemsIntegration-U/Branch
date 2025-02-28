package system.integration.branchInventory;

import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendEvent(String queueName, Object eventData) {
        try {
            String message = objectMapper.writeValueAsString(eventData); // Convertimos a JSON
            rabbitTemplate.convertAndSend(queueName, message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting event data to JSON", e);
        }
    }
}
