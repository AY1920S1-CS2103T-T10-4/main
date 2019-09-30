package seedu.address.model.gmaps;

import static java.util.Objects.requireNonNull;

/**
 * This class represents the edge of the location graph.
 * Where the edge stores the information of the travelling time and location it is
 * connected to.
 */

public class LocationEdge {

    public final Location location;

    public final int travellingTime;

    public LocationEdge(Location location, int travellingTime) {
        requireNonNull(location);
        this.location = location;
        this.travellingTime = travellingTime;
    }

    public String getLocationName() {
        return location.locationName;
    }

    @Override

    public boolean equals(Object obj) {
        return obj == this || obj instanceof LocationEdge
                && ((LocationEdge) obj).location.equals(location)
                && ((LocationEdge) obj).travellingTime == travellingTime;
    }

    @Override
    public String toString() {
        return travellingTime + "min to reach " + location.locationName;
    }
}
