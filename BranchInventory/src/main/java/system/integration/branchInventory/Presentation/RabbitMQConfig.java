package system.integration.branchInventory.Presentation;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Bean
    public CachingConnectionFactory connectionFactory() {
        Dotenv dotenv = Dotenv.load();
        CachingConnectionFactory factory = new CachingConnectionFactory();

        factory.setHost(dotenv.get("RABBITMQ_HOST"));
        factory.setPort(Integer.parseInt(dotenv.get("RABBITMQ_PORT")));
        factory.setUsername(dotenv.get("RABBITMQ_USERNAME"));
        factory.setPassword(dotenv.get("RABBITMQ_PASSWORD"));

        return factory;
    }
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
