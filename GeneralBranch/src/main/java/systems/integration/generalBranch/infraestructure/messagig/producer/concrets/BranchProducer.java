package systems.integration.generalBranch.infraestructure.messagig.producer.concrets;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

import systems.integration.generalBranch.infraestructure.messagig.event.IEvent;
import systems.integration.generalBranch.infraestructure.messagig.producer.interfaces.IBaseProducer;

public class BranchProducer implements IBaseProducer {
    @Override
    public void publish(String queueName, IEvent event, Channel channel) throws IOException, TimeoutException {
        channel.exchangeDeclare("branch.subscription.exchange", "fanout", true);

        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, "branch.subscription.exchange", "");
        channel.basicPublish(queueName, "", null, event.getBody().getBytes());
    }
}
