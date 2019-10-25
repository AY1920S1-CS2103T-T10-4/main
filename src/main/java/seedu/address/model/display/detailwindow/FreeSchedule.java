package seedu.address.model.display.detailwindow;

import java.time.DayOfWeek;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.HashMap;

import seedu.address.model.person.schedule.Venue;

/**
 * A FreeSchedule class that represents the free timeslot among the Persons for the week.
 */
public class FreeSchedule {

    private static final int DAYS_OF_THE_WEEK = 7;
    private static final LocalTime STARTTIME = LocalTime.of(00, 00);
    private static final LocalTime ENDTIME = LocalTime.of(23, 59);
    private HashMap<DayOfWeek, ArrayList<FreeTimeslot>> freeSchedule;

    public FreeSchedule(ArrayList<WeekSchedule> weekSchedules) {

        freeSchedule = new HashMap<>();

        for (int i = 1; i <= DAYS_OF_THE_WEEK; i++) {

            freeSchedule.put(DayOfWeek.of(i), new ArrayList<>());

            LocalTime currentTime = STARTTIME;
            ArrayList<Venue> lastVenues = new ArrayList<>();

            // initialize last venues to null for each person
            for (int j = 0; j < weekSchedules.size(); j++) {
                lastVenues.add(null);
            }

            boolean isClash;
            LocalTime newFreeStartTime = null;

            while (true) {

                isClash = false;

                // loop through each person
                for (int j = 0; j < weekSchedules.size(); j++) {
                    ArrayList<DayTimeslot> timeslots = weekSchedules.get(j).getWeekSchedule().get(DayOfWeek.of(i));

                    // loop through each timeslot
                    for (int k = 0; k < timeslots.size(); k++) {

                        // record the latest venue for each clash
                        if (timeslots.get(k).isClash(currentTime)) {
                            isClash = true;
                            lastVenues.set(j, timeslots.get(k).getVenue());
                            break;
                        }
                    }
                }

                if (!isClash) {
                    if (newFreeStartTime == null) {
                        newFreeStartTime = currentTime;
                    }
                } else {
                    if (newFreeStartTime != null) {
                        freeSchedule.get(DayOfWeek.of(i))
                                .add(new FreeTimeslot(new ArrayList<>(lastVenues), newFreeStartTime, currentTime));
                        newFreeStartTime = null;
                    }
                }

                if (currentTime.equals(ENDTIME)) {
                    if (!isClash) {
                        freeSchedule.get(DayOfWeek.of(i))
                                .add(new FreeTimeslot(new ArrayList<>(lastVenues), newFreeStartTime, currentTime));
                    }
                    break;
                }
                currentTime = currentTime.plusMinutes(1);
            }


        }

    }

    public HashMap<DayOfWeek, ArrayList<FreeTimeslot>> getFreeSchedule() {
        return freeSchedule;
    }
}
