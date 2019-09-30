package seedu.address.model.gmaps;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LocationVertexTest {
    private Location singapore = new Location("Singapore", "1.2345", "4.5678");
    @Test
    public void test_getLocationVertexName() {
        LocationVertex locationVertex = new LocationVertex(singapore);
        assertTrue(locationVertex.getLocationVertexName().equals("Singapore"));
    }

    @Test
    public void test_addEdgeToEmptyEdgeList() {
        Location malaysia = new Location("Malaysia", "1.2345", "4.5678");
        LocationEdge edgeToMalaysia = new LocationEdge(malaysia, 300);
        LocationVertex locationVertex = new LocationVertex(singapore);
        assertTrue(locationVertex.addEdge(edgeToMalaysia) instanceof LocationVertex);
        locationVertex = locationVertex.addEdge(edgeToMalaysia);
        assertTrue(locationVertex.getLocationEdge("Malaysia").equals(edgeToMalaysia));
    }

    @Test
    public void test_toString() {
        Location malaysia = new Location("Malaysia", "1.2345", "4.5678");
        LocationEdge edgeToMalaysia = new LocationEdge(malaysia, 300);
        LocationVertex locationVertex = new LocationVertex(singapore);
        assertTrue(locationVertex.toString().equals("Singapore:\n"));
        locationVertex = locationVertex.addEdge(edgeToMalaysia);
        assertTrue(locationVertex.toString().equals("Singapore:\n300min to reach Malaysia\n"));
    }
}
