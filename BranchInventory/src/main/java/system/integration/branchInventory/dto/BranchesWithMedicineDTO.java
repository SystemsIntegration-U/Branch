package system.integration.branchInventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.util.List;

@Value
@Getter
@Setter
@AllArgsConstructor
public class BranchesWithMedicineDTO {
    LocationDTO destination;
    List<LocationDTO> nearbyPoints;
    MedicineDTO medicineDTO;
    double range;
}
