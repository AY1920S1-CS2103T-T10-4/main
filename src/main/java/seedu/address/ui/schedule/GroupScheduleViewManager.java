package seedu.address.ui.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.model.display.scheduledisplay.ScheduleState;
import seedu.address.model.display.timeslots.FreeSchedule;
import seedu.address.model.display.timeslots.PersonSchedule;
import seedu.address.model.display.timeslots.WeekSchedule;
import seedu.address.model.group.GroupName;
import seedu.address.model.person.Name;

/**
 * Class to handle schedule view of groups
 */
public class GroupScheduleViewManager extends ScheduleViewManager {

    private List<PersonSchedule> originalPersonSchedules;
    private List<PersonSchedule> filteredPersonSchedules;
    private GroupName groupName;
    private ArrayList<FreeSchedule> freeSchedules;

    public GroupScheduleViewManager(List<PersonSchedule> originalPersonSchedules,
                                    GroupName groupName, ArrayList<FreeSchedule> freeSchedules) {
        this.originalPersonSchedules = originalPersonSchedules;
        this.filteredPersonSchedules = originalPersonSchedules;
        this.groupName = groupName;
        this.freeSchedules = freeSchedules;
        super.weekNumberShown = 0;
        super.currentDate = LocalDate.now();
        super.type = ScheduleState.GROUP;
        super.LOGGER.info("Generating schedule view for " + groupName.toString());
    }

    /**
     * Method to initialise or reinitialise group ScheduleView object to be displayed in the UI.
     * Group schedules show free time.
     */
    private void update() {
        LocalDate dateToShow = currentDate.plusDays(7 * weekNumberShown);
        ArrayList<WeekSchedule> weekSchedules = filteredPersonSchedules
                .stream().map(personSchedule -> personSchedule.getScheduleDisplay().get(weekNumberShown))
                .collect(Collectors.toCollection(ArrayList::new));
        super.scheduleView = new ScheduleView(weekSchedules, "Week " + (weekNumberShown + 1) + " "
                + groupName.toString(), dateToShow);
        //Required to set the free time schedule first before generating the schedule.
        super.scheduleView.setFreeTime(freeSchedules.get(weekNumberShown));
        super.scheduleView.generateSchedule();
    }

    /**
     * Filters the given person schedules with a given list of names.
     * @param filteredList List of names to be filtered.
     */
    private void filterPerson(List<Name> filteredList) {
        filteredPersonSchedules = new ArrayList<>();
        ArrayList<Name> filteredListCopy = new ArrayList<>(filteredList);
        for (PersonSchedule personSchedule : originalPersonSchedules) {
            if (filteredListCopy.contains(personSchedule.getPersonDisplay().getName())) {
                //Person is found in the filtered list.
                filteredPersonSchedules.add(personSchedule);
                filteredListCopy.remove(personSchedule.getPersonDisplay().getName());
            }
        }
        //Must remove everyone from filteredList
        assert filteredListCopy.size() == 0;
    }

    @Override
    public void filterPersonsFromSchedule(List<Name> namesToFilter) {
        filterPerson(namesToFilter);
    }

    @Override
    public void toggleNext() {
        super.weekNumberShown = (weekNumberShown + 1) % 4;
    }

    @Override
    public ScheduleView getScheduleViewCopy() {
        List<WeekSchedule> weekSchedules = new ArrayList<>();
        for (int i = 0; i < originalPersonSchedules.size(); i++) {
            weekSchedules.add(originalPersonSchedules.get(i).getScheduleDisplay().get(weekNumberShown));
        }

        ScheduleView copy = new ScheduleView(weekSchedules,
                groupName.toString(), currentDate.plusDays(7 * weekNumberShown));
        copy.setFreeTime(freeSchedules.get(weekNumberShown));
        copy.generateSchedule();
        return copy;
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
}
