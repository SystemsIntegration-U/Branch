package system.integration.branchInventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
@Getter
@Setter
@AllArgsConstructor
public class BranchDTO {
    LocationDTO location;
}
