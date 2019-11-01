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
        assertEquals("Avg distance: 0(meters)", result.getFirstAvg());
    }

    @Test
    void closestLocationDataString() {
        ArrayList<String> locationNameList = new ArrayList<>(Arrays.asList("LT17", "LT17", "LT17"));
        String result = gmapsModelManager.closestLocationDataString(locationNameList);
        String expectedResult = "\nFirst closest location: LT17 | Average travelling distance/meters Avg distance: "
                + "0(meters)\nSecond closest location: LT19 | Average travelling distance/meters Avg distance: "
                + "11(meters)\nThird closest location: LT8 | Average travelling distance/meters Avg distance: "
                + "224(meters)\n";
        assertEquals(expectedResult, result);
    }
}
