package seedu.address.model.gmaps;

import java.util.ArrayList;

/**
 * This represent the vertex of the location graph.
 * It contains information of the location of the vertex and
 * all the edges of the other location it is connected to
 */

public class LocationVertex {

    public final Location location;

    private ArrayList<LocationEdge> locationEdgeList = new ArrayList<LocationEdge>();

    public LocationVertex (Location location) {
        this.location = location;
    }

    public LocationVertex (Location location, ArrayList<LocationEdge> locationEdgeList) {
        this.location = location;
        this.locationEdgeList = locationEdgeList;
    }

    public String getLocationVertexName() {
        return location.locationName;
    }

    /**
     * This method gets the edge where the connect vertex tallies with the name
     * @param locationName
     * @return the edge that tallies with the name
     */

    public LocationEdge getLocationEdge(String locationName) {

        for (int i = 0; i < locationEdgeList.size(); i++) {
            if (locationEdgeList.get(i).getLocationName().equals(locationName)) {
                return locationEdgeList.get(i);
            }
        }
        throw new IllegalArgumentException("There are no edges with this locationName");
    }

    /**
     * This method is used to add location edge to to the vertex
     * @param locationEdge the edge to add to the vertex
     * @return the vertex with the added edge
     */
    public LocationVertex addEdge(LocationEdge locationEdge) {
        locationEdgeList.add(locationEdge);
        return new LocationVertex(location, locationEdgeList);
    }

    @Override

    public String toString() {
        String locationVertexName = getLocationVertexName();
        String edges = "";
        for (int i = 0; i < locationEdgeList.size(); i++) {
            edges = edges + locationEdgeList.get(i) + "\n";
        }
        return locationVertexName + ":\n" + edges;
    }

}
