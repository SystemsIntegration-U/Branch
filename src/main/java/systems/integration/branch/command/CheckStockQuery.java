package systems.integration.branch.command;

import java.util.UUID;

public class CheckStockQuery implements IRequest {
    private UUID medicineId;

    public CheckStockQuery(UUID medicineId) {
        this.medicineId = medicineId;
    }

    public UUID getMedicineId() {
        return medicineId;
    }
}
