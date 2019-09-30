package seedu.address.model.gmaps;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class LocationTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Location(null,null,null));
    }

    @Test
    public void test_toString() {
        Location location = new Location("Singapore", "1.2345","6.7890");
        assertTrue(location.toString().equals("Location: Singapore at latitude: 1.2345, longitude: 6.7890"));
    }
}
