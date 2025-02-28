package system.integration.branchInventory.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchDTO {
    private UUID id;
    private UUID medicineId; 
    private LocalDate expiryDate;
    private int stock;
}
