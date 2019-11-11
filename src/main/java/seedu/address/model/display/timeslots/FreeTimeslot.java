package seedu.address.model.display.timeslots;

import java.time.LocalTime;

import java.util.ArrayList;

import seedu.address.model.display.locationdata.ClosestCommonLocationData;

/**
 * A Free Timeslot class that represents the free time of the group schedule and the previous locations of the members.
 */
public class FreeTimeslot {

    private int id;

    private ArrayList<String> venues;
    private ClosestCommonLocationData closestCommonLocationData;

    private LocalTime startTime;
    private LocalTime endTime;

    public FreeTimeslot(int id,
                        ArrayList<String> venues,
                        ClosestCommonLocationData closestCommonLocationData,
                        LocalTime startTime,
                        LocalTime endTime) {

        this.id = id;
        this.venues = venues;
        this.closestCommonLocationData = closestCommonLocationData;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return this.id;
    }

    public ClosestCommonLocationData getClosestCommonLocationData() {
        return this.closestCommonLocationData;
    }

    public ArrayList<String> getVenues() {
        return venues;
    }

    public void setVenues(ArrayList<String> venues) {
        this.venues = venues;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
