package seedu.address.model.display.detailwindow;

import java.time.LocalTime;

import seedu.address.model.person.schedule.Venue;

/**
 * A timeslot of a day.
 */
public class PersonTimeslot {

    private String eventName;

    private LocalTime startTime;
    private LocalTime endTime;
    private Venue venue;


    public PersonTimeslot(String eventName, LocalTime startTime, LocalTime endTime, Venue venue) {
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;

    }

    public String getEventName() {
        return eventName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Venue getVenue() {
        return venue;
    }

    /**
     * Checks if there is a clash between the timeslot and given time.
     *
     * @param time to check for clash
     * @return boolean
     */
    public boolean isClash(LocalTime time) {
        if (time.isBefore(startTime) || time.isAfter(endTime) || time.compareTo(endTime) == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        String output = "";
        output += eventName + " "
                + startTime.toString() + " "
                + endTime.toString() + " "
                + venue.toString();

        return output;
    }
}
