package systems.integration.generalBranch.infraestructure.mapper;

import org.springframework.stereotype.Component;
import systems.integration.generalBranch.domain.model.Branch;
import systems.integration.generalBranch.domain.model.Location;
import systems.integration.generalBranch.presentation.DTOs.request.BranchDTO;
import systems.integration.generalBranch.presentation.DTOs.request.LocationDTO;

@Component
public class BranchMapper {

    public BranchDTO convertToDTO(Branch branch) {
        BranchDTO dto = new BranchDTO();
        dto.setGln(branch.getGln());
        dto.setName(branch.getName());
        if (branch.getLocation() != null) {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setLatitude(branch.getLocation().getLatitude());
            locationDTO.setLongitude(branch.getLocation().getLongitude());
            dto.setLocation(locationDTO);
        }
        return dto;
    }

    public Branch convertToEntity(BranchDTO dto) {
        Branch branch = new Branch();
        branch.setGln(dto.getGln());
        branch.setName(dto.getName());
        if (dto.getLocation() != null) {
            Location location = new Location();
            location.setLatitude(dto.getLocation().getLatitude());
            location.setLongitude(dto.getLocation().getLongitude());
            branch.setLocation(location);
        }
        return branch;
    }
}
