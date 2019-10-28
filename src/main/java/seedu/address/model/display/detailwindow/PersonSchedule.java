package seedu.address.model.display.detailwindow;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;

import seedu.address.model.display.sidepanel.PersonDisplay;
import seedu.address.model.mapping.Role;

/**
 * Schedule of timeslots for the week.
 */
public class PersonSchedule {

    private String scheduleName;

    private PersonDisplay personDisplay;

    private Role role;

    private HashMap<DayOfWeek, ArrayList<PersonTimeslot>> scheduleDisplay;

    public PersonSchedule(String scheduleName,
                          PersonDisplay personDisplay,
                          Role role, HashMap<DayOfWeek,
            ArrayList<PersonTimeslot>> scheduleDisplay) {

        this.scheduleName = scheduleName;
        this.personDisplay = personDisplay;
        this.role = role;
        this.scheduleDisplay = scheduleDisplay;
    }

    public PersonDisplay getPersonDisplay() {
        return this.personDisplay;
    }

    public Role getRole() {
        return this.role;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public HashMap<DayOfWeek, ArrayList<PersonTimeslot>> getScheduleDisplay() {
        return scheduleDisplay;
    }

    // for debugging purposes only
    @Override
    public String toString() {
        String output = "";

        output += "=====" + scheduleName + " for " + personDisplay.getName().toString() + "=====" + "\n";
        for (int i = 1; i <= 7; i++) {
            ArrayList<PersonTimeslot> personTimeslots = scheduleDisplay.get(DayOfWeek.of(i));
            output += DayOfWeek.of(i) + ":\n";
            for (PersonTimeslot d : personTimeslots) {
                String timeSlotDetails = d.getStartTime().toString() + "---" + d.getEndTime().toString();
                output += timeSlotDetails + "\n";
            }
        }
        return output;
    }
}
