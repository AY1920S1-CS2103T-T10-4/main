package seedu.address.logic.internal.gmaps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.TimeBookInvalidState;
import seedu.address.model.display.detailwindow.ClosestCommonLocationData;
import seedu.address.model.gmaps.Location;
import seedu.address.model.gmaps.LocationGraph;

class ClosestLocationTest {
    private ClosestLocation closestLocation;
    @BeforeEach
    void init() throws ConnectException, TimeBookInvalidState {
        ProcessVenues processVenues;
        processVenues = new ProcessVenues().process();
        ArrayList<Location> locations = processVenues.getLocations();
        ArrayList<String> validLocationList = processVenues.getValidLocationList();
        LocationGraph locationGraph = new LocationGraph(locations, validLocationList);
        new ProcessLocationGraph(locationGraph).process();
        closestLocation = new ClosestLocation(locationGraph);
    }

    @Test
    void closestLocationData() {
        ArrayList<String> locationNameList = new ArrayList<>(Arrays.asList("LT17", "LT17", "LT17"));
        ClosestCommonLocationData result = closestLocation.closestLocationData(locationNameList);
        assertEquals(result.getFirstClosest(), "LT17");
        assertEquals(result.getFirstAvg(), (long) 0);

    }

    @Test
    void closestLocation() {
        ArrayList<String> locationNameList = new ArrayList<>(Arrays.asList("LT17", "LT17", "LT17"));
        String result = closestLocation.closestLocationDataString(locationNameList);
        String expectedResult = "\nFirst closest location: LT17 | Average travelling distance/meters 0\n"
                + "Second closest location: LT19 | Average travelling distance/meters 11\n"
                + "Third closest location: AS4 | Average travelling distance/meters 224\n";
        assertEquals(expectedResult, result);
    }
}
