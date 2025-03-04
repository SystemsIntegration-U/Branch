package system.integration.branchInventory.Presentation.Consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import system.integration.branchInventory.dto.BranchDTO;
import system.integration.branchInventory.service.BranchEventService;

@Component
@Slf4j
public class BranchConsumer {

    private static final String BRANCH_SUBSCRIPTION_QUEUE = "additional_coordinates_queue";
    private final BranchEventService branchEventService;

    public BranchConsumer(BranchEventService branchEventService) {
        this.branchEventService = branchEventService;
    }

    @RabbitListener(queues = BRANCH_SUBSCRIPTION_QUEUE, ackMode = "MANUAL")
    public void receiveMessage(BranchDTO branchDto, Channel channel, Message message) {
        try {
            branchEventService.addEvent(branchDto);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("Received BranchDTO: ({}, {})",
                branchDto.getLocation().getLatitude(),
                branchDto.getLocation().getLongitude());}
        catch (Exception exception) {
            log.warn(exception.getMessage());
        }
    }
}