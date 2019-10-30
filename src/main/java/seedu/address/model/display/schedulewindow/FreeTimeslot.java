package seedu.address.model.display.schedulewindow;

import java.time.LocalTime;

import java.util.ArrayList;

import seedu.address.model.display.detailwindow.ClosestCommonLocationData;

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

        for (int i = 0; i < this.venues.size(); i++) {
            if (this.venues.get(i) == null) {
                this.venues.remove(i);
                i--;
            }
        }
    }

    /**
     * For debugging purposes only.
     */
    public String toString() {
        String s = "";
        s += "id: " + id + " === "
                + startTime.toString() + " - "
                + endTime.toString() + " "
                + venues + " "
                + "closest location: " + closestCommonLocationData.getFirstClosest() + "\n";
        return s;
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
