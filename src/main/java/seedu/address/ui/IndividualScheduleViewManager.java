package seedu.address.ui;

import java.time.LocalDate;
import java.util.List;

import seedu.address.model.display.detailwindow.MonthSchedule;
import seedu.address.model.display.sidepanel.PersonDisplay;

/**
 * Class to handle schedule views of individuals.
 */
public class IndividualScheduleViewManager implements ScheduleViewManager {
    private MonthSchedule monthSchedule;
    private String color;
    private ScheduleView scheduleView;
    private PersonDisplay personDisplay;
    private int weekNumber;

    public IndividualScheduleViewManager(MonthSchedule monthSchedule, PersonDisplay personDisplay, String color) {
        this.personDisplay = personDisplay;
        this.monthSchedule = monthSchedule;
        this.color = color;
        this.weekNumber = 0;
        initScheduleView();
    }

    /**
     * Method to initialise or reinitialise individual ScheduleView object to be displayed in the UI.
     * Individual schedules do not show free time.
     */
    private void initScheduleView() {
        LocalDate currentDate = LocalDate.now();
        LocalDate dateToShow = currentDate.plusDays(weekNumber * 7);
        this.scheduleView = new ScheduleView(List.of(monthSchedule.getWeekScheduleOf(weekNumber)),
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
    public void toggleNext() {
        this.weekNumber = (weekNumber + 1) % 4;
        initScheduleView();
    }
}
