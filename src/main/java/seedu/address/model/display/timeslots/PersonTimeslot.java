package seedu.address.model.display.timeslots;

import java.time.LocalDate;
import java.time.LocalTime;

import seedu.address.model.display.locationdata.ClosestCommonLocationData;
import seedu.address.model.person.schedule.Venue;

/**
 * A timeslot of a day.
 */
public class PersonTimeslot {

    private Integer id;
    private String color;
    private String eventName;
    private boolean isInGroup;

    private String displayString;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Venue venue;

    private ClosestCommonLocationData locationData;


    public PersonTimeslot(String eventName,
                          LocalDate date,
                          LocalTime startTime,
                          LocalTime endTime,
                          Venue venue,
                          String color,
                          boolean isInGroup,
                          ClosestCommonLocationData locationData) {
        this.id = 0;
        this.eventName = eventName;
        this.isInGroup = isInGroup;
        this.displayString = "-";
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
        this.color = color;
        this.locationData = locationData;
    }

    public String getDisplayString() {
        return displayString;
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

    public LocalDate getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        if (isInGroup) {
            displayString = Integer.toString(id);
        } else {
            displayString = id + ": " + eventName;
        }
    }

    public String getColor() {
        return color;
    }

    public ClosestCommonLocationData getLocationData() {
        return locationData;
    }

    /**
     * Checks if there is a clash between the timeslot and given time.
     *
     * @param time to check for clash
     * @return boolean
     */
    public boolean isClash(LocalTime time) {
        if (time.isBefore(startTime) || time.isAfter(endTime)) {
            return false;
        } else {
            return true;
        }
    }

}
