package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.display.detailwindow.ClosestCommonLocationData;

class GmapsModelManagerTest {
    private GmapsModelManager gmapsModelManager;
    @BeforeEach
    void setUp() {
        gmapsModelManager = new GmapsModelManager();
    }

    @Test
    void closestLocationData() {
        ArrayList<String> locationNameList = new ArrayList<>(Arrays.asList("LT17", "LT17", "LT17"));
        ClosestCommonLocationData result = gmapsModelManager.closestLocationData(locationNameList);
        assertEquals(result.getFirstClosest(), "LT17");
        assertEquals(result.getFirstAvg(), (long) 0);
    }

    @Test
    void closestLocationDataString() {
        ArrayList<String> locationNameList = new ArrayList<>(Arrays.asList("LT17", "LT17", "LT17"));
        String result = gmapsModelManager.closestLocationDataString(locationNameList);
        String expectedResult = "\nFirst closest location: LT17 | Average travelling distance/meters 0\n"
                + "Second closest location: LT19 | Average travelling distance/meters 11\n"
                + "Third closest location: AS4 | Average travelling distance/meters 224\n";
        assertEquals(expectedResult, result);
    }
}
