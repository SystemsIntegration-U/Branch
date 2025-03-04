package systems.integration.generalBranch.infraestructure.messagig.producer.concrets;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

import systems.integration.generalBranch.infraestructure.messagig.event.IEvent;
import systems.integration.generalBranch.infraestructure.messagig.producer.interfaces.IBaseProducer;

public class BranchProducer implements IBaseProducer {
    @Override
    public void publish(String queueName, String exchangeName, IEvent event, Channel channel)
            throws IOException, TimeoutException {
        channel.exchangeDeclare(exchangeName, "fanout", true);

        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, "");
        channel.basicPublish(exchangeName, "", null, event.getBody().getBytes());

        String additionalExchange = "additional_coordinates_exchange";
        String additionalQueue = "additional_coordinates_queue";

        channel.exchangeDeclare(additionalExchange, "fanout", true);
        channel.queueDeclare(additionalQueue, true, false, false, null);
        channel.queueBind(additionalQueue, additionalExchange, "");
        channel.basicPublish(additionalExchange, "", null, event.getBody().getBytes());
    }
}
