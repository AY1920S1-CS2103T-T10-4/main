package seedu.address.model.gmaps;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test LocationEdge object.
 */
public class LocationEdgeTest {

    @Test
    public void test_toString() {
        Location location = new Location("Singapore", "1.2345", "6.7890");
        LocationEdge locationEdge = new LocationEdge(location, 30);
        assertTrue(locationEdge.toString().equals("30min to reach Singapore"));
    }
}
