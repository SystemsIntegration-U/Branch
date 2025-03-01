package systems.integration.generalBranch.presentation.DTOs.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {
    @NotNull
    private Long gln;
    private String name;
    private LocationDTO location;
}
