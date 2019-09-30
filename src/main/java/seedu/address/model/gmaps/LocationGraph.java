package seedu.address.model.gmaps;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * This is the graph object that contains the information for location vertex
 */
public class LocationGraph {
    private Hashtable<String, LocationVertex> graph = new Hashtable<>();
    private ArrayList<String> orderedKeys = new ArrayList<>();

    LocationGraph() {
    }

    LocationGraph(Hashtable<String, LocationVertex> graph, ArrayList<String> orderedeKeys) {
        this.graph = graph;
        this.orderedKeys = orderedeKeys;
    }

    /**
     * Add vertex to the graph
     * @param locationVertex
     * @return a new LocationGraph with the newly added LocationGraph
     */

    public LocationGraph addVertex(LocationVertex locationVertex) {
        graph.put(locationVertex.getLocationVertexName(), locationVertex);
        orderedKeys.add(locationVertex.getLocationVertexName());
        return new LocationGraph(graph, orderedKeys);
    }

    public LocationVertex getVertex(String locationName) {
        return graph.get(locationName);
    }

    @Override
    public String toString() {
        String returnString = "Location graph:\n\n";
        for (int i = 0; i < orderedKeys.size(); i++) {
            String currKey = orderedKeys.get(i);
            LocationVertex currVertex = graph.get(currKey);
            returnString = returnString + currVertex.toString();
        }
        return returnString;
    }
}
