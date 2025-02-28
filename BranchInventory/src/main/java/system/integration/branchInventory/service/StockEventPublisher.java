package system.integration.branchInventory.service;

import org.springframework.stereotype.Service;

import system.integration.branchInventory.RabbitMQEventPublisher;
import system.integration.branchInventory.domain.model.Medicine;

@Service
public class StockEventPublisher {
    private final RabbitMQEventPublisher eventPublisher;

    public StockEventPublisher(RabbitMQEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void checkAndPublishStockEvent(Medicine medicine) {
        if (medicine.getStock() < 10) {
            String event = String.format("Restock needed: %s has only %d units.", medicine.getName(), medicine.getStock());
            eventPublisher.sendEvent("restock_queue", event);
        }        
    }
}


