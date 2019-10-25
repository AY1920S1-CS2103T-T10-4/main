package seedu.address.logic.internal.gmaps;

import java.net.ConnectException;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import seedu.address.commons.exceptions.TimeBookInvalidLocation;
import seedu.address.websocket.Cache;

/**
 * This call is used to find the valid location name
 */
public class SanitizeLocation {
    private ArrayList<String> validLocationList = new ArrayList<>();
    /**
     * Takes in gmapsApi so that it could be replaced by a gmapsApi stub
     */
    public SanitizeLocation() {
    }

    /**
     * This method is used to get the list of sanitized location
     * @return
     */
    public ArrayList<String> getValidLocationList() {
        return this.validLocationList;
    }

    /**
     * This method is used to santize the location name on nus mods to a google maps identifiable location
     * @param locationName
     * @return
     * @throws ConnectException
     */
    public String sanitize(String locationName) throws TimeBookInvalidLocation {
        String validLocation = "NUS_" + locationName;
        validLocation = validLocation.split("-")[0];
        if (!validLocationList.contains(validLocation)) {
            JSONObject apiResponse = Cache.loadPlaces(validLocation);
            String status = GmapsJsonUtils.getStatus(apiResponse);
            if (status.equals("OK")) {
                validLocationList.add(validLocation);
            } else {
                validLocation = "NUS_" + validLocation.split("_")[1];
                if (!validLocationList.contains(validLocation)) {
                    apiResponse = Cache.loadPlaces(validLocation);
                    status = GmapsJsonUtils.getStatus(apiResponse);
                    if (status.equals("OK")) {
                        validLocationList.add(validLocation);
                    } else {
                        throw new TimeBookInvalidLocation("Cannot identify " + validLocation);
                    }
                }
            }
        }
        return validLocation;
    }
}
