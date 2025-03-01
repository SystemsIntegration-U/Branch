package systems.integration.generalBranch.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LocationTests {

    @Test
    public void testLocationEntity() {
        Location location = new Location();
        location.setLatitude(40.7128);
        location.setLongitude(-74.0060);

        assertEquals(40.7128, location.getLatitude());
        assertEquals(-74.0060, location.getLongitude());

        Location location2 = new Location();
        location2.setLatitude(location.getLatitude());
        location2.setLongitude(location.getLongitude());

        assertEquals(location, location2);
        assertEquals(location.hashCode(), location2.hashCode());
        assertNotNull(location.toString());
    }
}