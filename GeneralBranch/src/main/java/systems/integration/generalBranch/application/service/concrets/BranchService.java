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
import systems.integration.generalBranch.utils.DatabaseCreator;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

@Service
public class BranchService extends GenericService<Branch, UUID> implements IBranchService {

    private final IBranchRepository branchRepository;
    private static final int BASE_PORT = 8000;
    private static final int MAX_PORT = 8079;
    private static final String POSTGRES_URL = "jdbc:postgresql://postgres:5432/";
    private static final String POSTGRES_USER = System.getenv("POSTGRES_USER");
    private static final String POSTGRES_PASSWORD = System.getenv("POSTGRES_PASSWORD");

    public BranchService(IBranchRepository branchRepository) {
        super(branchRepository);
        this.branchRepository = branchRepository;
    }

    @Override
    public Branch save(Branch branch) {
        int nextPort = findNextAvailablePort();
        if (nextPort > MAX_PORT) {
            throw new RuntimeException("No hay más puertos disponibles en el rango 8000-8079");
        }
        branch.setPort(nextPort);

        Branch savedBranch = super.save(branch);

        String dbName = "branch_" + branch.getGln();
        DatabaseCreator.createDatabase(dbName);

        startSpecificBranchInstance(dbName, nextPort);

        return savedBranch;
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
                    // Nombre del contenedor y base de datos
                    String containerName = "specificbranch_branch_" + gln;
                    String dbName = "branch_" + gln;

                    // Eliminar el registro de la base de datos principal
                    branchRepository.deleteById(branch.getId());

                    // Detener y eliminar el contenedor
                    stopAndRemoveContainer(containerName);

                    // Eliminar la base de datos específica
                    dropDatabase(dbName);

                    return true;
                }).orElse(false);
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
            System.out.println("Iniciando creación de contenedor para " + dbName + " en puerto " + port);
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
                            "SERVER_PORT=" + String.valueOf(port)
                    )
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

            // Detener el contenedor si está corriendo
            dockerClient.stopContainerCmd(containerName).withTimeout(10).exec();
            System.out.println("Contenedor " + containerName + " detenido");

            // Eliminar el contenedor
            dockerClient.removeContainerCmd(containerName).exec();
            System.out.println("Contenedor " + containerName + " eliminado");
        } catch (Exception e) {
            System.err.println("Error al detener/eliminar contenedor " + containerName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void dropDatabase(String dbName) {
        try (Connection conn = DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             Statement stmt = conn.createStatement()) {
            // Terminar todas las conexiones activas a la base de datos
            stmt.execute("SELECT pg_terminate_backend(pg_stat_activity.pid) " +
                    "FROM pg_stat_activity " +
                    "WHERE pg_stat_activity.datname = '" + dbName + "' AND pid <> pg_backend_pid()");

            // Eliminar la base de datos
            stmt.execute("DROP DATABASE IF EXISTS " + dbName);
            System.out.println("Base de datos " + dbName + " eliminada exitosamente");
        } catch (SQLException e) {
            System.err.println("Error al eliminar la base de datos " + dbName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}