package systems.integration.generalBranch.presentation.DTOs.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    private Double latitude;
    private Double longitude;
}
