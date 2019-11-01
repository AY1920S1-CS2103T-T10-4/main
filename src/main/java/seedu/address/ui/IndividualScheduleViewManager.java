package seedu.address.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import seedu.address.model.display.detailwindow.PersonSchedule;
import seedu.address.model.display.schedulewindow.ScheduleWindowDisplayType;
import seedu.address.model.display.sidepanel.PersonDisplay;

/**
 * Class to handle schedule views of individuals. Schedule of individuals do not show free time.
 */
public class IndividualScheduleViewManager implements ScheduleViewManager {
    private HashMap<Integer, ArrayList<PersonSchedule>> monthSchedule;
    private String color;
    private ScheduleView scheduleView;
    private PersonDisplay personDisplay;
    private int weekNumber;
    private LocalDate currentDate;

    public IndividualScheduleViewManager(HashMap<Integer, ArrayList<PersonSchedule>> monthSchedule,
                                         PersonDisplay personDisplay, String color) {
        this.personDisplay = personDisplay;
        this.monthSchedule = monthSchedule;
        this.color = color;
        this.weekNumber = 0;
        this.currentDate = LocalDate.now();
        initScheduleView();
    }

    /**
     * Method to initialise or reinitialise individual ScheduleView object to be displayed in the UI.
     * Individual schedules do not show free time.
     */
    private void initScheduleView() {
        LocalDate dateToShow = currentDate.plusDays(weekNumber * 7);
        this.scheduleView = new ScheduleView(monthSchedule.get(weekNumber),
                List.of(color), personDisplay.getName().fullName, dateToShow);
    }

    @Override
    public ScheduleView getScheduleView() {
        return this.scheduleView;
    }

    @Override
    public void scrollNext() {
        this.scheduleView.scrollNext();
    }

    @Override
    public ScheduleView getScheduleViewCopy() {
        return new ScheduleView(monthSchedule.get(weekNumber), List.of(color), personDisplay.getName().fullName,
                currentDate);
    }

    @Override
    public void toggleNext() {
        this.weekNumber = (weekNumber + 1) % 4;
        initScheduleView();
    }

    @Override
    public List<String> getColors() {
        return List.of(color);
    }

    @Override
    public ScheduleWindowDisplayType getScheduleWindowDisplayType() {
        return ScheduleWindowDisplayType.PERSON;
    }
}
