package seedu.address.ui.schedule;

import java.time.LocalDate;
import java.util.List;

import seedu.address.model.display.scheduledisplay.ScheduleState;
import seedu.address.model.display.timeslots.PersonSchedule;
import seedu.address.model.person.Name;

/**
 * Class to handle schedule views of individuals. Schedule of individuals do not show free time.
 */
public class IndividualScheduleViewManager extends ScheduleViewManager {
    private PersonSchedule personSchedule;

    public IndividualScheduleViewManager(PersonSchedule personSchedule) {
        this.personSchedule = personSchedule;
        super.weekNumberShown = 0;
        super.currentDate = LocalDate.now();
        super.type = ScheduleState.PERSON;
        super.LOGGER.info("Generating schedule for " + personSchedule.getPersonDisplay().getName().fullName);
    }

    /**
     * Method to initialise or reinitialise individual ScheduleView object to be displayed in the UI.
     * Individual schedules do not show free time.
     */
    private void update() {
        LocalDate dateToShow = currentDate.plusDays(weekNumberShown * 7);
        super.scheduleView = new ScheduleView(List.of(personSchedule
                .getScheduleDisplay().get(weekNumberShown)), "Week " + (weekNumberShown + 1) + " "
                + personSchedule.getPersonDisplay().getName().fullName, dateToShow);
        super.scheduleView.generateSchedule();
    }

    @Override
    public ScheduleView getScheduleView() {
        update();
        return super.scheduleView;
    }

    @Override
    public void scrollNext() {
        super.scheduleView.scrollNext();
    }

    @Override
    public ScheduleView getScheduleViewCopy() {
        ScheduleView copy = new ScheduleView(List.of(personSchedule
                .getScheduleDisplay().get(weekNumberShown)),
                personSchedule.getPersonDisplay().getName().fullName, currentDate.plusDays(7 * weekNumberShown));
        copy.generateSchedule();
        return copy;
    }

    @Override
    public void toggleNext() {
        super.weekNumberShown = (weekNumberShown + 1) % 4;
    }

    @Override
    public void filterPersonsFromSchedule(List<Name> persons) {
        //Cannot filter persons from individual schedule.
    }
}
