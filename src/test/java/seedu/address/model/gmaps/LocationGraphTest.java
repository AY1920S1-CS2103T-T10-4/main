package seedu.address.model.gmaps;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class LocationGraphTest {
    @Test
    public void test_addVertex() {
        LocationGraph locationGraph = new LocationGraph();
        Location singapore = new Location("Singapore", "1.23", "4.56");
        Location malaysia = new Location("Malaysia", "1.23", "4.56");
        LocationEdge toMalaysia = new LocationEdge(malaysia, 300);
        LocationVertex singaporeVertex = new LocationVertex(singapore);
        assertTrue(locationGraph.addVertex(singaporeVertex) instanceof LocationGraph);
    }

    @Test
    public void test_getVertex() {
        LocationGraph locationGraph = new LocationGraph();
        Location singapore = new Location("Singapore", "1.23", "4.56");
        Location malaysia = new Location("Malaysia", "1.23", "4.56");
        LocationEdge toMalaysia = new LocationEdge(malaysia, 300);
        LocationVertex singaporeVertex = new LocationVertex(singapore);
        locationGraph = locationGraph.addVertex(singaporeVertex);
        assertTrue(locationGraph.getVertex("Singapore").equals(singaporeVertex));
    }

    @Test
    public void test_toString() {
        LocationGraph locationGraph = new LocationGraph();
        Location singapore = new Location("Singapore", "1.23", "4.56");
        Location malaysia = new Location("Malaysia", "1.23", "4.56");
        LocationEdge toMalaysia = new LocationEdge(malaysia, 300);
        LocationVertex singaporeVertex = new LocationVertex(singapore);
        locationGraph = locationGraph.addVertex(singaporeVertex);
        String expectedString = "Location graph:\n\n" + singaporeVertex.toString();
        assertTrue(locationGraph.toString().equals(expectedString));
    }

}
