package system.integration.branchInventory.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockAlertEventDTO {
    private UUID medicineId;
    private String atc;
    private int requiredQuantity;
}
