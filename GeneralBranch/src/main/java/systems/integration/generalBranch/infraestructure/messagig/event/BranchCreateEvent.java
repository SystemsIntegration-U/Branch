package systems.integration.generalBranch.infraestructure.messagig.event;

import systems.integration.generalBranch.domain.model.Location;

public class BranchCreateEvent implements IEvent {

    private Long gln;
    private Location location;

    public BranchCreateEvent(Long gln, Location location) {
        this.gln = gln;
        this.location = location;
    }

    @Override
    public String getBody() {
        return String.format("{\"gln\": \"%s\", \"location\": \"%s\"}", gln, location);
    }

}
