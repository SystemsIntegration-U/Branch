package system.integration.branchInventory.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import system.integration.branchInventory.dto.BranchDTO;
import system.integration.branchInventory.dto.LocationDTO;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class BranchEventService {

    private final Set<LocationDTO> processedLocations = new HashSet<>();

    public void addEvent(BranchDTO event) {
        LocationDTO location = event.getLocation();

        if (processedLocations.contains(location)) {
            return;
        }

        processedLocations.add(location);
        log.info("Processing new branch with location: ({}, {})", location.getLatitude(), location.getLongitude());
    }

    public Set<LocationDTO> getAllProcessedLocations() {
        return processedLocations;
    }

    public LocationDTO generateRandomLocation() {
        double latitude = -90 + Math.random() * 180;
        double longitude = -180 + Math.random() * 360;
        return new LocationDTO(latitude, longitude);
    }
}
