package seedu.address.model.display.timeslots;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.display.locationdata.ClosestCommonLocationData;
import seedu.address.model.person.schedule.Venue;
import seedu.address.ui.util.ColorGenerator;

class PersonTimeslotTest {

    private final int id = 1;
    private final String eventName = "eventName";
    private final LocalDate date = LocalDate.of(2019, 11, 11);
    private final LocalTime startTime = LocalTime.of(10, 30);
    private final LocalTime endTime = LocalTime.of(11, 30);
    private final Venue venue = new Venue("venue");
    private final String color = ColorGenerator.generateColor(0);

    private PersonTimeslot personTimeslot;
    private PersonTimeslot personTimeslotGroup;
    private ClosestCommonLocationData closestCommonLocationData;

    @BeforeEach
    void init() {
        closestCommonLocationData = new ClosestCommonLocationData();
        closestCommonLocationData.setErrorResponse("error");

        personTimeslot = new PersonTimeslot(
                eventName,
                date,
                startTime,
                endTime,
                venue,
                color,
                false,
                closestCommonLocationData
        );

        personTimeslotGroup = new PersonTimeslot(
                eventName,
                date,
                startTime,
                endTime,
                venue,
                color,
                true,
                closestCommonLocationData
        );
    }

    @Test
    void getDisplayString_default() {
        assertEquals("-", personTimeslot.getDisplayString());
    }

    @Test
    void getDisplayString_inGroup() {
        personTimeslotGroup.setId(id);
        assertEquals(Integer.toString(id), personTimeslotGroup.getDisplayString());
    }

    @Test
    void getDisplayString_inPerson() {
        personTimeslot.setId(id);
        assertEquals(id + ": " + eventName, personTimeslot.getDisplayString());
    }

    @Test
    void getEventName() {
        assertEquals(eventName, personTimeslot.getEventName());
    }

    @Test
    void getStartTime() {
        assertEquals(startTime, personTimeslot.getStartTime());
    }

    @Test
    void getEndTime() {
        assertEquals(endTime, personTimeslot.getEndTime());
    }

    @Test
    void getVenue() {
        assertEquals(venue, personTimeslot.getVenue());
    }

    @Test
    void getDate() {
        assertEquals(date, personTimeslot.getDate());
    }

    @Test
    void getId() {
        personTimeslot.setId(id);
        assertEquals(id, personTimeslot.getId());
    }

    @Test
    void getColor() {
        assertEquals(color, personTimeslot.getColor());
    }

    @Test
    void getLocationData() {
        assertEquals(closestCommonLocationData.toString(),
                personTimeslot.getLocationData().toString());
    }

    @Test
    void isClash_true() {
        assertTrue(personTimeslot.isClash(LocalTime.of(11, 0)));
        assertTrue(personTimeslot.isClash(LocalTime.of(10, 30)));
        assertTrue(personTimeslot.isClash(LocalTime.of(11, 30)));
    }

    @Test
    void isClash_false() {
        assertFalse(personTimeslot.isClash(LocalTime.of(12, 0)));
        assertFalse(personTimeslot.isClash(LocalTime.of(10, 29)));
        assertFalse(personTimeslot.isClash(LocalTime.of(11, 31)));
    }

}
