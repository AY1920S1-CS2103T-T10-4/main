package seedu.address.logic.internal.gmaps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void init() throws TimeBookInvalidState {
        ProcessVenues processVenues;
        processVenues = new ProcessVenues().process();
        ArrayList<Location> locations = processVenues.getLocations();
        ArrayList<Location> validLocationList = processVenues.getValidLocationList();
        ProcessLocationGraph processLocationGraph = new ProcessLocationGraph(validLocationList);
        LocationGraph locationGraph = new LocationGraph(locations, validLocationList,
                processLocationGraph.getDistanceMatrix());
        closestLocation = new ClosestLocation(locationGraph);
    }

    @Test
    void happyFlowAllLocationValid() {
        ArrayList<String> locationNameList = new ArrayList<>(Arrays.asList("LT17", "LT17", "LT17"));
        ClosestCommonLocationData result = closestLocation.closestLocationData(locationNameList);
        assertEquals(result.getFirstClosest(), "LT17");
        assertEquals("Avg distance: 0(meters)", result.getFirstAvg());
        assertEquals(result.getLocationEntered(), locationNameList);
        assertEquals(result.getValidLocation(), locationNameList);
    }

    @Test
    void happyFlowSomeLocationValid() {
        ArrayList<String> locationNameList1 = new ArrayList<>(Arrays.asList("canteen", "LT17", "BIAP"));
        ClosestCommonLocationData result1;
        result1 = closestLocation.closestLocationData((ArrayList<String>) locationNameList1.clone());
        assertTrue(result1.isOk());
        assertEquals(result1.getFirstClosest(), "LT17");
        assertEquals(new ArrayList<String>(Arrays.asList("BIAP", "canteen")), result1.getInvalidLocation());
        assertEquals(locationNameList1, result1.getLocationEntered());
        assertEquals(new ArrayList<>(Arrays.asList("LT17")), result1.getValidLocation());

    }

    @Test
    void sadFlowAllLocationNotIdentifiable() {
        ArrayList<String> locationNameList = new ArrayList<>(Arrays.asList("canteen", "canteen", "canteen"));
        ClosestCommonLocationData result;
        result = closestLocation.closestLocationData((ArrayList<String>) locationNameList.clone());
        assertFalse(result.isOk());
        assertEquals(locationNameList, result.getLocationEntered());
        assertNull(result.getValidLocation());
        assertEquals(result.getErrorResponse(),
                "All location entered cannot be identified by TimeBook. Refer to  Supported Location table "
                        + "in User Guide to ge the supported locations.\n"
                        + "Source location: canteen, canteen, canteen\n"
                        + "Invalid Source location: canteen, canteen, canteen\n");
        assertEquals(new ArrayList<String>(Arrays.asList("canteen", "canteen", "canteen")),
                result.getInvalidLocation());
    }

    @Test
    void sadFlowNoLocationEntered() {
        ArrayList<String> locationNameList = new ArrayList<>();
        ClosestCommonLocationData result;
        result = closestLocation.closestLocationData(locationNameList);
        assertEquals(locationNameList, result.getLocationEntered());
        assertFalse(result.isOk());
        assertEquals(result.getErrorResponse(), "You must enter at least one location.\n");
    }

    @Test
    void closestLocation() {
        ArrayList<String> locationNameList = new ArrayList<>(Arrays.asList("LT17", "LT17", "LT17"));
        String result = closestLocation.closestLocationDataString(locationNameList);
        String expectedResult = "\n"
                + "First closest location: LT17 | Average travelling distance/meters Avg distance: 0(meters)\n"
                + "Second closest location: LT19 | Average travelling distance/meters Avg distance: 11(meters)\n"
                + "Third closest location: LT8 | Average travelling distance/meters Avg distance: 224(meters)\n";
        assertEquals(expectedResult, result);
    }
}
