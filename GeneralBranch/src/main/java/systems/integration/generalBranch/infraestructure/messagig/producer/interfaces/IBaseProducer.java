package systems.integration.generalBranch.infraestructure.messagig.producer.interfaces;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import systems.integration.generalBranch.infraestructure.messagig.event.IEvent;

public interface IBaseProducer {
    void publish(String queueName, IEvent event, Channel channel) throws IOException, TimeoutException;
}
