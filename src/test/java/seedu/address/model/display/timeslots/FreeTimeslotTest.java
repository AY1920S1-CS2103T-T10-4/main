package seedu.address.model.display.timeslots;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.display.locationdata.ClosestCommonLocationData;

class FreeTimeslotTest {

    private FreeTimeslot freeTimeslot;

    private int id;
    private ArrayList<String> venues;
    private ClosestCommonLocationData closestCommonLocationData;
    private LocalTime startTime;
    private LocalTime endTime;

    @BeforeEach
    void init() {

        id = 1;
        venues = new ArrayList<>(List.of("venue1", "venue2"));
        closestCommonLocationData = new ClosestCommonLocationData();
        closestCommonLocationData.setErrorResponse("error");
        startTime = LocalTime.of(10, 30);
        endTime = LocalTime.of(11, 30);

        freeTimeslot = new FreeTimeslot(
                id,
                venues,
                closestCommonLocationData,
                startTime,
                endTime
        );
    }

    @Test
    void getId() {
        assertEquals(id, freeTimeslot.getId());
    }

    @Test
    void getClosestCommonLocationData() {
        assertEquals(freeTimeslot.getClosestCommonLocationData().toString(),
                closestCommonLocationData.toString());
    }

    @Test
    void getVenues() {
        freeTimeslot.setVenues(venues);
        assertEquals(venues, freeTimeslot.getVenues());
    }

    @Test
    void getStartTime() {
        assertEquals(startTime, freeTimeslot.getStartTime());
    }

    @Test
    void setStartTime() {
        LocalTime newStartTime = LocalTime.of(11, 0);
        freeTimeslot.setStartTime(newStartTime);
        assertEquals(newStartTime, freeTimeslot.getStartTime());
    }

    @Test
    void getEndTime() {
        assertEquals(endTime, freeTimeslot.getEndTime());
    }

    @Test
    void setEndTime() {
        LocalTime newEndTime = LocalTime.of(12, 0);
        freeTimeslot.setEndTime(newEndTime);
        assertEquals(newEndTime, freeTimeslot.getEndTime());
    }
}
