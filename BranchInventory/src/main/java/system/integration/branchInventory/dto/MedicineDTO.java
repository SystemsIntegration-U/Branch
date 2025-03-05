package system.integration.branchInventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.util.UUID;

@Value
@Getter
@Setter
@AllArgsConstructor
public class MedicineDTO {
    UUID id;
    String name;
    int stock;
}
