package seedu.address.model.display;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import seedu.address.model.GmapsModelManager;
import seedu.address.model.TimeBook;
import seedu.address.model.display.detailwindow.ClosestCommonLocationData;
import seedu.address.model.display.detailwindow.PersonSchedule;
import seedu.address.model.display.detailwindow.PersonTimeslot;
import seedu.address.model.display.schedulewindow.FreeSchedule;
import seedu.address.model.display.schedulewindow.FreeTimeslot;
import seedu.address.model.display.schedulewindow.ScheduleWindowDisplay;
import seedu.address.model.display.schedulewindow.ScheduleWindowDisplayType;
import seedu.address.model.display.sidepanel.GroupDisplay;
import seedu.address.model.display.sidepanel.PersonDisplay;
import seedu.address.model.display.sidepanel.SidePanelDisplay;
import seedu.address.model.display.sidepanel.SidePanelDisplayType;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupId;
import seedu.address.model.group.GroupName;
import seedu.address.model.group.exceptions.GroupNotFoundException;
import seedu.address.model.mapping.Role;
import seedu.address.model.mapping.exceptions.MappingNotFoundException;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;
import seedu.address.model.person.User;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.person.schedule.Event;
import seedu.address.model.person.schedule.Schedule;
import seedu.address.model.person.schedule.Timeslot;
import seedu.address.model.person.schedule.Venue;

/**
 * Handler for all display models.
 */
public class DisplayModelManager {

    private static final int DAYS_OF_A_WEEK = 7;
    private static final int FREE_TIMESLOT_TRHESHOLD = 15;

    private GmapsModelManager gmapsModelManager;

    private LocalTime startTime;
    private LocalTime endTime;

    private ScheduleWindowDisplay scheduleWindowDisplay;
    private SidePanelDisplay sidePanelDisplay;

    public DisplayModelManager(GmapsModelManager gmapsModelManager) {
        this.gmapsModelManager = gmapsModelManager;
        this.startTime = LocalTime.of(8, 0);
        this.endTime = LocalTime.of(19, 0);
    }

    public DisplayModelManager(GmapsModelManager gmapsModelManager, LocalTime startTime, LocalTime endTime) {
        this.gmapsModelManager = gmapsModelManager;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Updates the detail window display.
     *
     * @param scheduleWindowDisplay
     */
    public void updateScheduleWindowDisplay(ScheduleWindowDisplay scheduleWindowDisplay) {
        this.scheduleWindowDisplay = scheduleWindowDisplay;
    }

    /**
     * Updates with a schedule of a person.
     *
     * @param name     of person's schedule to be updated
     * @param time     start time of the schedule
     * @param type     type of schedule
     * @param timeBook data
     */
    public void updateScheduleWindowDisplay(Name name, LocalDateTime time,
                                            ScheduleWindowDisplayType type,
                                            TimeBook timeBook) {

        try {
            HashMap<Integer, ArrayList<PersonSchedule>> personMonthSchedules = new HashMap<>();
            for (int i = 0; i < 4; i++) {
                ArrayList<PersonSchedule> personSchedulesForWeek = new ArrayList<>();

                PersonSchedule personSchedule =
                        generatePersonSchedule(name.toString(), time.plusDays(i * DAYS_OF_A_WEEK),
                                timeBook.getPersonList().findPerson(name), Role.emptyRole());
                personSchedulesForWeek.add(personSchedule);
                personMonthSchedules.put(i, personSchedulesForWeek);
            }
            ScheduleWindowDisplay scheduleWindowDisplay = new ScheduleWindowDisplay(personMonthSchedules, type);
            updateScheduleWindowDisplay(scheduleWindowDisplay);

        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates with a schedule of the user.
     *
     * @param time     start time of the schedule
     * @param type     type of schedule
     * @param timeBook data
     */
    public void updateScheduleWindowDisplay(LocalDateTime time, ScheduleWindowDisplayType type, TimeBook timeBook) {
        User user = timeBook.getPersonList().getUser();

        HashMap<Integer, ArrayList<PersonSchedule>> personSchedules = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            ArrayList<PersonSchedule> personSchedulesForWeek = new ArrayList<>();

            PersonSchedule personSchedule = generatePersonSchedule(user.getName().toString(),
                    time.plusDays(i * DAYS_OF_A_WEEK), user, Role.emptyRole());
            personSchedulesForWeek.add(personSchedule);
            personSchedules.put(i, personSchedulesForWeek);
        }

        ScheduleWindowDisplay scheduleWindowDisplay = new ScheduleWindowDisplay(personSchedules, type);
        updateScheduleWindowDisplay(scheduleWindowDisplay);
    }

    /**
     * Update with a schedule of a group.
     *
     * @param groupName of the group
     * @param now       start time of the schedule
     * @param type      type of schedule
     * @param timeBook  data
     */
    public void updateScheduleWindowDisplay(GroupName groupName,
                                            LocalDateTime now,
                                            ScheduleWindowDisplayType type,
                                            TimeBook timeBook) {

        try {

            Group group = timeBook.getGroupList().findGroup(groupName);
            GroupId groupId = group.getGroupId();
            GroupDisplay groupDisplay = new GroupDisplay(group);

            ArrayList<PersonId> personIds = timeBook.getPersonToGroupMappingList()
                    .findPersonsOfGroup(group.getGroupId());
            HashMap<Integer, ArrayList<PersonSchedule>> combinedMonthsSchedules = new HashMap<>();
            ArrayList<FreeSchedule> freeScheduleForMonth = new ArrayList<>();
            for (int h = 0; h < 4; h++) {
                ArrayList<PersonSchedule> personSchedules = new ArrayList<>();

                User user = timeBook.getPersonList().getUser();
                Role userRole = group.getUserRole();

                //Add user schedule.
                personSchedules.add(generatePersonSchedule(groupName.toString(),
                        now.plusDays(h * DAYS_OF_A_WEEK), user, userRole));

                //Add other schedules.
                for (int i = 0; i < personIds.size(); i++) {
                    Person person = timeBook.getPersonList().findPerson(personIds.get(i));
                    Role role = timeBook.getPersonToGroupMappingList().findRole(personIds.get(i), groupId);
                    if (role == null) {
                        role = Role.emptyRole();
                    }
                    PersonSchedule personSchedule = generatePersonSchedule(groupName.toString(),
                            now.plusDays(h * DAYS_OF_A_WEEK),
                            person, role);
                    personSchedules.add(personSchedule);
                }

                FreeSchedule freeSchedule = generateFreeSchedule(personSchedules, now);
                combinedMonthsSchedules.put(h, personSchedules);
                freeScheduleForMonth.add(freeSchedule);
            }
            ScheduleWindowDisplay scheduleWindowDisplay =
                    new ScheduleWindowDisplay(combinedMonthsSchedules, freeScheduleForMonth, groupDisplay, type);
            updateScheduleWindowDisplay(scheduleWindowDisplay);

        } catch (GroupNotFoundException | MappingNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the side panel display.
     *
     * @param sidePanelDisplay to be updated
     */
    public void updateSidePanelDisplay(SidePanelDisplay sidePanelDisplay) {
        this.sidePanelDisplay = sidePanelDisplay;
    }

    /**
     * Updates the side panel display of type.
     *
     * @param type     of side panel display to be updated
     * @param timeBook data
     */
    public void updateSidePanelDisplay(SidePanelDisplayType type, TimeBook timeBook) {

        SidePanelDisplay sidePanelDisplay;

        ArrayList<PersonDisplay> displayPersons = new ArrayList<>();
        ArrayList<GroupDisplay> displayGroups = new ArrayList<>();

        ArrayList<Person> persons = timeBook.getPersonList().getPersons();
        ArrayList<Group> groups = timeBook.getGroupList().getGroups();

        for (int i = 0; i < persons.size(); i++) {
            displayPersons.add(new PersonDisplay(persons.get(i)));
        }

        for (int i = 0; i < groups.size(); i++) {
            displayGroups.add(new GroupDisplay(groups.get(i)));
        }

        sidePanelDisplay = new SidePanelDisplay(displayPersons, displayGroups, type);
        updateSidePanelDisplay(sidePanelDisplay);

    }

    /**
     * Getter method to retrieve detail window display.
     *
     * @return ScheduleWindowDisplay
     */
    public ScheduleWindowDisplay getScheduleWindowDisplay() {
        return scheduleWindowDisplay;
    }

    /**
     * Getter method to retrieve side panel display.
     *
     * @return SidePanelDisplay
     */
    public SidePanelDisplay getSidePanelDisplay() {
        return sidePanelDisplay;
    }

    /**
     * Generates the PersonSchedule of a Person.
     *
     * @param scheduleName name of the schedule
     * @param now          current time
     * @param person       of the schedule
     * @param role         role of the person
     * @return PersonSchedule
     */
    private PersonSchedule generatePersonSchedule(String scheduleName, LocalDateTime now, Person person, Role role) {

        HashMap<DayOfWeek, ArrayList<PersonTimeslot>> scheduleDisplay = new HashMap<>();

        Schedule personSchedule = person.getSchedule();
        ArrayList<Event> events = personSchedule.getEvents();

        for (int i = 1; i <= DAYS_OF_A_WEEK; i++) {
            scheduleDisplay.put(DayOfWeek.of(i), new ArrayList<>());
        }

        for (int e = 0; e < events.size(); e++) {
            Event currentEvent = events.get(e);
            String eventName = currentEvent.getEventName();

            ArrayList<Timeslot> timeslots = currentEvent.getTimeslots();
            for (int t = 0; t < timeslots.size(); t++) {
                Timeslot currentTimeslot = timeslots.get(t);

                LocalDateTime currentStartTime = currentTimeslot.getStartTime();
                LocalDateTime currentEndTime = currentTimeslot.getEndTime();

                Venue currentVenue = currentTimeslot.getVenue();

                //Checks to see if the currentStartTime is within the upcoming 7 days.
                if (now.toLocalDate().plusDays(DAYS_OF_A_WEEK).isAfter(currentStartTime.toLocalDate())
                        && now.toLocalDate().minusDays(1).isBefore(currentStartTime.toLocalDate())
                        && (startTime.isBefore(currentStartTime.toLocalTime())
                        || startTime.compareTo(currentStartTime.toLocalTime()) == 0)
                        && endTime.isAfter(currentStartTime.toLocalTime())) {

                    PersonTimeslot timeslot = new PersonTimeslot(
                            eventName,
                            currentStartTime.toLocalTime(),
                            currentEndTime.toLocalTime().isAfter(endTime) ? endTime : currentEndTime.toLocalTime(),
                            currentVenue
                    );

                    scheduleDisplay.get(currentStartTime.getDayOfWeek()).add(timeslot);
                    scheduleDisplay.get(currentStartTime.getDayOfWeek()).sort(
                            Comparator.comparing(PersonTimeslot::getStartTime)
                    );
                }
            }
        }

        return new PersonSchedule(scheduleName, new PersonDisplay(person, role), scheduleDisplay);
    }

    /**
     * Generates a free schedule from a list of person schedules.
     *
     * @param personSchedules to generate the free schedule from
     * @return FreeSchedule
     */
    private FreeSchedule generateFreeSchedule(ArrayList<PersonSchedule> personSchedules, LocalDateTime now) {

        HashMap<DayOfWeek, ArrayList<FreeTimeslot>> freeSchedule = new HashMap<>();

        int idCounter = 1;

        int currentDay = now.getDayOfWeek().getValue();

        for (int i = currentDay; i <= DAYS_OF_A_WEEK + currentDay - 1; i++) {

            int day = i;
            if (i > 7) {
                day -= 7;
            }

            System.out.println("Day test: " + DayOfWeek.of(day).toString());

            freeSchedule.put(DayOfWeek.of(day), new ArrayList<>());

            LocalTime currentTime = startTime;
            ArrayList<String> lastVenues = new ArrayList<>();

            // initialize last venues to null for each person
            for (int j = 0; j < personSchedules.size(); j++) {
                lastVenues.add(null);
            }

            boolean isClash;
            LocalTime newFreeStartTime = null;

            while (true) {

                isClash = false;

                ArrayList<String> currentLastVenues = new ArrayList<>(lastVenues);

                // loop through each person
                for (int j = 0; j < personSchedules.size(); j++) {
                    ArrayList<PersonTimeslot> timeslots = personSchedules.get(j)
                            .getScheduleDisplay().get(DayOfWeek.of(day));

                    // loop through each timeslot
                    for (int k = 0; k < timeslots.size(); k++) {

                        // record the latest venue for each clash
                        if (timeslots.get(k).isClash(currentTime)) {
                            isClash = true;
                            currentLastVenues.set(j, timeslots.get(k).getVenue().toString());
                            break;
                        }
                    }
                }

                if (!isClash) {
                    if (newFreeStartTime == null) {
                        newFreeStartTime = currentTime;
                    }
                    lastVenues = new ArrayList<>(currentLastVenues);

                } else {
                    if (newFreeStartTime != null) {

                        if (newFreeStartTime.until(currentTime, MINUTES) >= FREE_TIMESLOT_TRHESHOLD - 1) {
                            ArrayList<String> temp = new ArrayList<>(lastVenues);
                            for (int arr = 0; arr < temp.size(); arr++) {
                                if (temp.get(arr) == null) {
                                    temp.remove(arr);
                                    arr--;
                                }
                            }

                            ClosestCommonLocationData closestCommonLocationData =
                                    gmapsModelManager.closestLocationData(temp);

                            freeSchedule.get(DayOfWeek.of(day))
                                    .add(new FreeTimeslot(
                                            idCounter,
                                            new ArrayList<>(lastVenues),
                                            closestCommonLocationData,
                                            newFreeStartTime,
                                            currentTime));

                            idCounter++;
                        }

                        newFreeStartTime = null;
                    }
                    lastVenues = new ArrayList<>(currentLastVenues);

                }

                if (currentTime.equals(endTime)) {
                    if (!isClash) {

                        if (newFreeStartTime.until(currentTime, MINUTES) >= FREE_TIMESLOT_TRHESHOLD - 1) {
                            ArrayList<String> temp = new ArrayList<>(lastVenues);
                            for (int arr = 0; arr < temp.size(); arr++) {
                                if (temp.get(arr) == null) {
                                    temp.remove(arr);
                                    arr--;
                                }
                            }
                            ClosestCommonLocationData closestCommonLocationData =
                                    gmapsModelManager.closestLocationData(temp);

                            freeSchedule.get(DayOfWeek.of(day))
                                    .add(new FreeTimeslot(
                                            idCounter,
                                            new ArrayList<>(lastVenues),
                                            closestCommonLocationData,
                                            newFreeStartTime,
                                            currentTime));
                            idCounter++;
                        }

                    }
                    break;
                }
                currentTime = currentTime.plusMinutes(1);
            }

        }

        return new FreeSchedule(freeSchedule);
    }

    public ScheduleWindowDisplayType getState() {
        return scheduleWindowDisplay.getScheduleWindowDisplayType();
    }
}
