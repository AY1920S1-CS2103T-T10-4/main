package seedu.address.logic.internal.gmaps;

import java.net.ConnectException;
import java.util.ArrayList;

import org.json.simple.JSONArray;

import seedu.address.commons.exceptions.TimeBookInvalidLocation;
import seedu.address.commons.exceptions.TimeBookInvalidState;
import seedu.address.model.gmaps.Location;
import seedu.address.websocket.Cache;
import seedu.address.websocket.GmapsApi;

/**
 * This class is used to get nus venues
 */
public class ProcessVenues {
    private JSONArray venuesNusMods;
    private ArrayList<Location> venues = new ArrayList<>();
    private transient GmapsApi gmapsApi = new GmapsApi();
    private SanitizeLocation sanitizeLocation = new SanitizeLocation();

    public ProcessVenues(){
    }
    private ProcessVenues(JSONArray venuesNusMods, ArrayList<Location> venues, SanitizeLocation sanitizeLocation) {
        this.venues = venues;
        this.sanitizeLocation = sanitizeLocation;
        this.venuesNusMods = venuesNusMods;
    }

    public ArrayList<Location> getLocations() throws TimeBookInvalidState {
        if (venuesNusMods == null) {
            throw new TimeBookInvalidState("Cannot call getLocation before getVenuesJsonArray");
        }
        return this.venues;
    }

    /**
     * This method is used to process the venues with the latest information from NUSmods and Google Maps
     * @return
     * @throws ConnectException
     */
    public ProcessVenues process() throws ConnectException {
        ProcessVenues processVenuesWNusMods = getVenuesJsonArray();
        ProcessVenues processVenuesWVenues = processVenuesWNusMods.populateVenues();
        return processVenuesWVenues;
    }

    public ArrayList<String> getValidLocationList() {
        return sanitizeLocation.getValidLocationList();
    }

    /**
     * This method is used to process the nus mods venues api.
     * @return
     * @throws ConnectException
     */
    private ProcessVenues populateVenues() throws ConnectException {
        if (venuesNusMods == null) {
            throw new IllegalStateException("Cannot call getLocation before calling get"
                    + "getVenuesJsonArray");
        } else {
            for (int i = 0; i < venuesNusMods.size(); i++) {
                System.out.println("Processing " + venuesNusMods.get(i) + " " + i + "/" + venuesNusMods.size());
                Location currLocation = getLocation(i);
                venues.add(currLocation);
            }
        }
        return new ProcessVenues(venuesNusMods, venues, sanitizeLocation);
    }

    private ProcessVenues getVenuesJsonArray() {
        JSONArray currVenuesNusMod = Cache.loadVenues();
        return new ProcessVenues(currVenuesNusMod, venues, sanitizeLocation);
    }

    private Location getLocation(int i) throws ConnectException {
        if (venuesNusMods == null) {
            throw new IllegalStateException("Cannot call getLocation before calling get"
                   + "getVenuesJsonArray");
        } else {
            String locationName = (String) venuesNusMods.get(i);
            Location currLocation = new Location(locationName);
            try {
                String validLocation = sanitizeLocation.sanitize(locationName);
                currLocation.setValidLocation(validLocation);
                System.out.println(locationName + " identified as " + validLocation);
            } catch (TimeBookInvalidLocation e) {
                System.out.println(e.getMessage());
            }
            return currLocation;
        }
    }
}
