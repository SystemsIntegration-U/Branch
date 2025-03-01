package systems.integration.generalBranch.infraestructure.messagig.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLContext;

public class RabbitMQConfig {
    private static Connection connection;

    private RabbitMQConfig() {
    }

    public static Connection getConnection() throws IOException, TimeoutException, NoSuchAlgorithmException {
        if (connection == null || !connection.isOpen()) {
            ConnectionFactory factory = new ConnectionFactory();
            configCredentials(factory);
            connection = factory.newConnection();
        }
        return connection;
    }

    private static void configCredentials(ConnectionFactory factory) throws NoSuchAlgorithmException {
        Dotenv dotenv = Dotenv.load();
        factory.setHost(dotenv.get("RABBITMQ_HOST"));
        factory.setPort(Integer.parseInt(dotenv.get("RABBITMQ_PORT")));
        factory.setUsername(dotenv.get("RABBITMQ_USERNAME"));
        factory.setPassword(dotenv.get("RABBITMQ_PASSWORD"));
        factory.useSslProtocol(SSLContext.getDefault());
    }

    public static void closeConnection() throws IOException {
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }
}
