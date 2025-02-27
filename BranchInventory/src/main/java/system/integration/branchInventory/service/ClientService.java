package system.integration.branchInventory.service;

import org.springframework.stereotype.Service;
import system.integration.branchInventory.domain.model.Client;
import system.integration.branchInventory.repository.IClientRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {
    private final IClientRepository clientRepository;

    public ClientService(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(UUID id) {
        return clientRepository.findById(id).orElse(null);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public void deleteClient(UUID id) {
        clientRepository.deleteById(id);
    }
}