package systems.integration.generalBranch.infraestructure.messagig.event;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import systems.integration.generalBranch.domain.model.Location;

@Data
public class BranchEvent implements IEvent {

    @JsonManagedReference
    private Location location;

    public BranchEvent(Location location) {
        this.location = location;
    }

    @Override
    public String getBody() {
        return String.format(
                "{\"location\": {\"latitude\": %f, \"longitude\": %f}}",
                location.getLatitude(),
                location.getLongitude());
    }
}
