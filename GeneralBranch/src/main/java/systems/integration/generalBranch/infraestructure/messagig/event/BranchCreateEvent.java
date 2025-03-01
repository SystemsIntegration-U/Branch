package systems.integration.generalBranch.infraestructure.messagig.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import systems.integration.generalBranch.domain.model.Location;

@Data
public class BranchCreateEvent implements IEvent {

    private Long gln;
    private Location location;

    public BranchCreateEvent(Long gln, Location location) {
        this.gln = gln;
        this.location = location;
    }

    @Override
    public String getBody() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error to generate the JSON", e);
        }
    }
}
