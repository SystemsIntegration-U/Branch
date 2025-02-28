package system.integration.branchInventory.service;

import java.util.HashMap;
import java.util.Map;

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
        if (medicine.getStock() == 10) {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("medicineId", medicine.getId());
            eventData.put("atc", medicine.getAtc());
            eventData.put("requiredQuantity", 10);

            eventPublisher.sendEvent("low_stock_queue", eventData);
        }
    }
}

