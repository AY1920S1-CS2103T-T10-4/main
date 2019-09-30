package seedu.address.model.gmaps;

import static java.util.Objects.requireNonNull;

/**
 * Represent a location object for the various venues in NUS
 */

public class Location {

    public final String locationName;
    public final String latitude;
    public final String longitude;

    public Location(String locationName, String latitude, String longitude) {
        requireNonNull(locationName);
        requireNonNull(latitude);
        requireNonNull(longitude);
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override

    public boolean equals(Object other) {
        return other == this || other instanceof Location
                && ((Location) other).locationName.equals(locationName)
                && ((Location) other).latitude.equals(latitude)
                && ((Location) other).longitude.equals(longitude);
    }

    @Override
    public String toString() {
        return "Location: " + locationName + " at latitude: " + latitude + ", longitude: " + longitude;
    }
}
