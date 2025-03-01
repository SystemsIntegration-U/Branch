package systems.integration.generalBranch.domain.model;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class BranchTests {

    @Test
    public void testBranchEntity() {
        Location location = new Location();
        location.setLatitude(40.7128);
        location.setLongitude(-74.0060);

        Branch branch = new Branch();
        branch.setId(UUID.randomUUID());
        branch.setGln(1234567890123L);
        branch.setName("Main Branch");
        branch.setLocation(location);
        branch.setPort(8080);

        assertNotNull(branch.getId());
        assertEquals(1234567890123L, branch.getGln());
        assertEquals("Main Branch", branch.getName());
        assertEquals(location, branch.getLocation());
        assertEquals(8080, branch.getPort());

        Branch branch2 = new Branch();
        branch2.setId(branch.getId());
        branch2.setGln(branch.getGln());
        branch2.setName(branch.getName());
        branch2.setLocation(branch.getLocation());
        branch2.setPort(branch.getPort());

        assertEquals(branch, branch2);
        assertEquals(branch.hashCode(), branch2.hashCode());
        assertNotNull(branch.toString());
    }
}