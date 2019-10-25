package seedu.address.logic.internal.gmaps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.TimeBookInvalidLocation;

class SanitizeLocationTest {
    private SanitizeLocation sanitizeLocation;

    @BeforeEach
    void init() {
        sanitizeLocation = new SanitizeLocation();
    }

    @Test
    void getValidLocationList() throws TimeBookInvalidLocation, ConnectException {
        try {
            sanitizeLocation.sanitize("LT17");
            sanitizeLocation.sanitize("AS5-1234556");
            sanitizeLocation.sanitize("blah");
        } catch (TimeBookInvalidLocation e) {
            System.out.println(e.getMessage());
        }
        ArrayList<String> expectedValidLocationList =
                new ArrayList<String>(Arrays.asList("NUS_LT17", "NUS_AS5"));
        assertEquals(expectedValidLocationList, sanitizeLocation.getValidLocationList());
    }

    @Test
    void sanitize() throws TimeBookInvalidLocation {
        assertEquals(sanitizeLocation.sanitize("LT17"), "NUS_LT17");
        assertEquals(sanitizeLocation.sanitize("AS5-1234556"), "NUS_AS5");
        assertThrows(TimeBookInvalidLocation.class, () -> sanitizeLocation.sanitize("jcdhsajkfebadbs"));
    }
}
