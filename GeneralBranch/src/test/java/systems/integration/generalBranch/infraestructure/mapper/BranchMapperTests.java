package systems.integration.generalBranch.infraestructure.mapper;

import org.junit.jupiter.api.Test;
import systems.integration.generalBranch.domain.model.Branch;
import systems.integration.generalBranch.domain.model.Location;
import systems.integration.generalBranch.presentation.DTOs.request.BranchDTO;
import systems.integration.generalBranch.presentation.DTOs.request.LocationDTO;
import static org.junit.jupiter.api.Assertions.*;

public class BranchMapperTests {

    @Test
    public void testConvertToDTO() {
        Location location = new Location();
        location.setLatitude(40.7128);
        location.setLongitude(-74.0060);

        Branch branch = new Branch();
        branch.setGln(1234567890123L);
        branch.setName("Main Branch");
        branch.setLocation(location);

        BranchMapper branchMapper = new BranchMapper();

        BranchDTO branchDTO = branchMapper.convertToDTO(branch);

        assertEquals(branch.getGln(), branchDTO.getGln());
        assertEquals(branch.getName(), branchDTO.getName());
        assertNotNull(branchDTO.getLocation());
        assertEquals(branch.getLocation().getLatitude(), branchDTO.getLocation().getLatitude());
        assertEquals(branch.getLocation().getLongitude(), branchDTO.getLocation().getLongitude());
    }

    @Test
    public void testConvertToDTO_WithoutLocation() {
        Branch branch = new Branch();
        branch.setGln(1234567890123L);
        branch.setName("Main Branch");

        BranchMapper branchMapper = new BranchMapper();

        BranchDTO branchDTO = branchMapper.convertToDTO(branch);

        assertEquals(branch.getGln(), branchDTO.getGln());
        assertEquals(branch.getName(), branchDTO.getName());
        assertNull(branchDTO.getLocation());
    }

    @Test
    public void testConvertToEntity() {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setLatitude(40.7128);
        locationDTO.setLongitude(-74.0060);

        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setGln(1234567890123L);
        branchDTO.setName("Main Branch");
        branchDTO.setLocation(locationDTO);

        BranchMapper branchMapper = new BranchMapper();

        Branch branch = branchMapper.convertToEntity(branchDTO);

        assertEquals(branchDTO.getGln(), branch.getGln());
        assertEquals(branchDTO.getName(), branch.getName());
        assertNotNull(branch.getLocation());
        assertEquals(branchDTO.getLocation().getLatitude(), branch.getLocation().getLatitude());
        assertEquals(branchDTO.getLocation().getLongitude(), branch.getLocation().getLongitude());
    }

    @Test
    public void testConvertToEntity_WithoutLocation() {
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setGln(1234567890123L);
        branchDTO.setName("Main Branch");

        BranchMapper branchMapper = new BranchMapper();

        Branch branch = branchMapper.convertToEntity(branchDTO);

        assertEquals(branchDTO.getGln(), branch.getGln());
        assertEquals(branchDTO.getName(), branch.getName());
        assertNull(branch.getLocation());
    }
}