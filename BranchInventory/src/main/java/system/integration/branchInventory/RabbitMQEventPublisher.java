package system.integration.branchInventory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEvent(String queueName, String mensaje) {
        rabbitTemplate.convertAndSend(queueName, mensaje);
        System.out.println("Event sent to queue  " + queueName + ": " + mensaje);
    }
}