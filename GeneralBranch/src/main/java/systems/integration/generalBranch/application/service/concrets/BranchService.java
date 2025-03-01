package systems.integration.generalBranch.application.service.concrets;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;

import systems.integration.generalBranch.application.service.interfaces.IBranchService;
import systems.integration.generalBranch.domain.model.Branch;
import systems.integration.generalBranch.domain.repository.interfaces.IBranchRepository;
import systems.integration.generalBranch.infraestructure.messagig.config.RabbitMQConfig;
import systems.integration.generalBranch.infraestructure.messagig.event.BranchEvent;
import systems.integration.generalBranch.infraestructure.messagig.producer.concrets.BranchProducer;
import systems.integration.generalBranch.infraestructure.messagig.producer.interfaces.IBaseProducer;
import systems.integration.generalBranch.utils.DatabaseCreator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
public class BranchService extends GenericService<Branch, UUID> implements IBranchService {

    private final IBranchRepository branchRepository;
    private final IBaseProducer producer;
    private static final int BASE_PORT = 8000;
    private static final int MAX_PORT = 8079;
    private static final String POSTGRES_URL = "jdbc:postgresql://postgres:5432/";
    private static final String POSTGRES_USER = System.getenv("POSTGRES_USER");
    private static final String POSTGRES_PASSWORD = System.getenv("POSTGRES_PASSWORD");

    public BranchService(IBranchRepository branchRepository) {
        super(branchRepository);
        this.branchRepository = branchRepository;
        this.producer = new BranchProducer();
    }

    @Override
    public Branch save(Branch branch) {
        int nextPort = findNextAvailablePort();
        if (nextPort > MAX_PORT) {
            throw new RuntimeException("There are no more ports available in the range 8000-8079");
        }
        branch.setPort(nextPort);

        Branch savedBranch = super.save(branch);

        String dbName = "branch_" + branch.getGln();
        DatabaseCreator.createDatabase(dbName);
        publishCreateBrancchEvent(savedBranch);
        startSpecificBranchInstance(dbName, nextPort);
        return savedBranch;
    }

    private void publishCreateBrancchEvent(Branch branch) {
        try {
            com.rabbitmq.client.Connection connection = RabbitMQConfig.getConnection();
            BranchEvent event = new BranchEvent(branch.getLocation());
            producer.publish("branch.subscription.queue",
                    "branch.subscription.exchange", event, connection.createChannel());
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public Optional<Branch> findByGln(Long gln) {
        return branchRepository.findByGln(gln);
    }

    public Branch updateByGln(Long gln, Branch entity) {
        Branch existing = branchRepository.findByGln(gln)
                .orElseThrow(() -> new RuntimeException("Branch not found with GLN: " + gln));
        entity.setId(existing.getId());
        entity.setPort(existing.getPort());
        return branchRepository.save(entity);
    }

    public boolean deleteByGln(Long gln) {
        return branchRepository.findByGln(gln)
                .map(branch -> {
                    String containerName = "specificbranch_branch_" + gln;
                    String dbName = "branch_" + gln;

                    branchRepository.deleteById(branch.getId());
                    publishDeleteBrancchEvent(branch);

                    stopAndRemoveContainer(containerName);

                    dropDatabase(dbName);

                    return true;
                }).orElse(false);
    }

    private void publishDeleteBrancchEvent(Branch branch) {
        try {
            com.rabbitmq.client.Connection connection = RabbitMQConfig.getConnection();
            BranchEvent event = new BranchEvent(branch.getLocation());
            producer.publish("branch.unsubscription.queue", "branch.subscription.exchange", event,
                    connection.createChannel());
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private int findNextAvailablePort() {
        Integer maxPort = branchRepository.findMaxPort();
        if (maxPort == null || maxPort < BASE_PORT) {
            return BASE_PORT;
        }
        return maxPort + 1;
    }

    public void startSpecificBranchInstance(String dbName, int port) {
        try {
            DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost("unix:///var/run/docker.sock")
                    .build();

            DockerClient dockerClient = DockerClientBuilder.getInstance(config)
                    .withDockerCmdExecFactory(new NettyDockerCmdExecFactory())
                    .build();

            ExposedPort exposedPort = ExposedPort.tcp(port);
            HostConfig hostConfig = HostConfig.newHostConfig()
                    .withPortBindings(new PortBinding(Ports.Binding.bindPort(port), exposedPort))
                    .withNetworkMode("branch_default");

            CreateContainerResponse container = dockerClient.createContainerCmd("specificbranch:latest")
                    .withEnv(
                            "DB_NAME=" + dbName,
                            "POSTGRES_USER=" + System.getenv("POSTGRES_USER"),
                            "POSTGRES_PASSWORD=" + System.getenv("POSTGRES_PASSWORD"),
                            "SERVER_PORT=" + String.valueOf(port))
                    .withExposedPorts(exposedPort)
                    .withHostConfig(hostConfig)
                    .withName("specificbranch_" + dbName)
                    .exec();

            System.out.println("Contenedor creado con ID: " + container.getId());
            dockerClient.startContainerCmd(container.getId()).exec();
            System.out.println("Contenedor iniciado para " + dbName + " en puerto " + port);
        } catch (Exception e) {
            System.err.println("Error al iniciar SpecificBranch: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void stopAndRemoveContainer(String containerName) {
        try {
            DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost("unix:///var/run/docker.sock")
                    .build();

            DockerClient dockerClient = DockerClientBuilder.getInstance(config)
                    .withDockerCmdExecFactory(new NettyDockerCmdExecFactory())
                    .build();

            dockerClient.stopContainerCmd(containerName).withTimeout(10).exec();

            dockerClient.removeContainerCmd(containerName).exec();
        } catch (Exception e) {
            System.err.println("Error stopping/deleting container " + containerName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void dropDatabase(String dbName) {
        try (Connection conn = DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD);
                Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT pg_terminate_backend(pg_stat_activity.pid) " +
                    "FROM pg_stat_activity " +
                    "WHERE pg_stat_activity.datname = '" + dbName + "' AND pid <> pg_backend_pid()");

            stmt.execute("DROP DATABASE IF EXISTS " + dbName);
        } catch (SQLException e) {
            System.err.println("Error deleting database " + dbName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}