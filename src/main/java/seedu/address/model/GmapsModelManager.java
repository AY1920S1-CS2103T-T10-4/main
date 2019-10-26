package seedu.address.model;

import java.net.ConnectException;
import java.util.ArrayList;

import seedu.address.commons.exceptions.TimeBookInvalidState;
import seedu.address.logic.internal.gmaps.ClosestLocation;
import seedu.address.logic.internal.gmaps.ProcessLocationGraph;
import seedu.address.logic.internal.gmaps.ProcessVenues;
import seedu.address.model.display.detailwindow.ClosestCommonLocationData;
import seedu.address.model.gmaps.Location;
import seedu.address.model.gmaps.LocationGraph;

/**
 * Represent the in memory of Gmaps related model
 */
public class GmapsModelManager {

    private ArrayList<Location> locations;

    private ArrayList<String> validLocationList;

    private LocationGraph locationGraph;

    public GmapsModelManager() {
        try {
            initProcessVenues();
            initLocationGraph();
        } catch (TimeBookInvalidState | ConnectException e) {
            e.printStackTrace();
        }
    }

    public ClosestCommonLocationData closestLocationData(ArrayList<String> locationNameList) {
        return new ClosestLocation(locationGraph).closestLocationData(locationNameList);
    }

    public String closestLocationDataString(ArrayList<String> locationNameList) {
        return new ClosestLocation(locationGraph).closestLocationDataString(locationNameList);
    }

    /**
     * Method used to initProcess venues and get location list and validLocationList
     */
    private void initProcessVenues() throws TimeBookInvalidState {
        ProcessVenues processVenues;
        processVenues = new ProcessVenues().process();
        locations = processVenues.getLocations();
        validLocationList = processVenues.getValidLocationList();
    }

    /**
     * Method used to initialise the full location graph
     * @throws TimeBookInvalidState
     * @throws ConnectException
     */
    private void initLocationGraph() throws TimeBookInvalidState, ConnectException {
        locationGraph = new LocationGraph(locations, validLocationList);
        new ProcessLocationGraph(locationGraph).process();
    }
}
