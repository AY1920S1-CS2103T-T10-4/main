package seedu.address.model.display.detailwindow;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClosestCommonLocationDataTest {
    private ClosestCommonLocationData closestCommonLocationData;
    @BeforeEach
    void init() {
        closestCommonLocationData = new ClosestCommonLocationData();
    }

    @Test
    void getErrorResponseAllLocationInvalid() {
        ArrayList<String> input = new ArrayList<>(Arrays.asList("FOO", "BAR", "HELLO"));
        closestCommonLocationData.setErrorResponse("ERROR: FOO BAR");
        closestCommonLocationData.setLocationEntered(input);
        closestCommonLocationData.setInvalidLocation(input);
        assertEquals("ERROR: FOO BAR\n"
                + "Source location: FOO, BAR, HELLO\n"
                + "Invalid Source location: FOO, BAR, HELLO\n", closestCommonLocationData.getErrorResponse());
    }

    @Test
    void getErrorResponseSomeLocationInvalid() {
        ArrayList<String> input = new ArrayList<>(Arrays.asList("FOO", "BAR", "HELLO"));
        closestCommonLocationData.setErrorResponse("ERROR: FOO BAR");
        closestCommonLocationData.setLocationEntered(input);
        closestCommonLocationData.setValidLocation(new ArrayList<>(Arrays.asList("HELLO")));
        closestCommonLocationData.setInvalidLocation(new ArrayList<>(Arrays.asList("FOO", "BAR")));
        assertEquals("ERROR: FOO BAR\n"
                + "Source location: FOO, BAR, HELLO\n"
                + "Valid Source location: HELLO\n"
                + "Invalid Source location: FOO, BAR\n", closestCommonLocationData.getErrorResponse());
    }

    @Test
    void getErrorResponseAllValid() {
        ArrayList<String> input = new ArrayList<>(Arrays.asList("FOO", "BAR", "HELLO"));
        closestCommonLocationData.setErrorResponse("ERROR: FOO BAR");
        closestCommonLocationData.setLocationEntered(input);
        closestCommonLocationData.setValidLocation(input);
        assertEquals("ERROR: FOO BAR\n"
                + "Source location: FOO, BAR, HELLO\n"
                + "Valid Source location: FOO, BAR, HELLO\n", closestCommonLocationData.getErrorResponse());
    }

    @Test
    void getImage() {
        closestCommonLocationData.setFirstClosest("LT17");
        assertDoesNotThrow(()->closestCommonLocationData.getImage());
    }

    @Test
    void getFirstClosest() {
        closestCommonLocationData.setFirstClosest("FOO");
        assertEquals("FOO", closestCommonLocationData.getFirstClosest());
    }

    @Test
    void getSecondClosest() {
        closestCommonLocationData.setSecondClosest("FOO");
        assertEquals("FOO", closestCommonLocationData.getSecondClosest());
    }

    @Test
    void getThirdClosest() {
        closestCommonLocationData.setThirdClosest("FOO");
        assertEquals("FOO", closestCommonLocationData.getThirdClosest());
    }

    @Test
    void getFirstAvg() {
        closestCommonLocationData.setFirstAvg((long) Long.MAX_VALUE);
        assertEquals("Avg distance: 9223372036854775807(meters)", closestCommonLocationData.getFirstAvg());
    }

    @Test
    void getSecondAvg() {
        closestCommonLocationData.setSecondAvg((long) Long.MAX_VALUE);
        assertEquals("Avg distance: 9223372036854775807(meters)", closestCommonLocationData.getSecondAvg());
    }

    @Test
    void getThirdAvg() {
        closestCommonLocationData.setThirdAvg((long) Long.MAX_VALUE);
        assertEquals("Avg distance: 9223372036854775807(meters)", closestCommonLocationData.getThirdAvg());
    }

    @Test
    void getInvalidLocation() {
        assertNull(closestCommonLocationData.getInvalidLocation());
        ArrayList<String> input = new ArrayList<>(Arrays.asList("FOO", "BAR", "HELLO"));
        closestCommonLocationData.setInvalidLocation(input);
        assertEquals(input, closestCommonLocationData.getInvalidLocation());
    }

    @Test
    void isOk() {
        assertEquals(closestCommonLocationData.isOk(), false);
        closestCommonLocationData.setOk(true);
        assertEquals(closestCommonLocationData.isOk(), true);
    }
}
