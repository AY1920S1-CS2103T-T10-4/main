package seedu.address.model.display;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.GmapsModelManager;
import seedu.address.model.display.locationdata.ClosestCommonLocationData;
import seedu.address.model.display.scheduledisplay.GroupScheduleDisplay;
import seedu.address.model.display.scheduledisplay.HomeScheduleDisplay;
import seedu.address.model.display.scheduledisplay.PersonScheduleDisplay;
import seedu.address.model.display.scheduledisplay.ScheduleDisplay;
import seedu.address.model.display.scheduledisplay.ScheduleState;
import seedu.address.model.display.sidepanel.GroupDisplay;
import seedu.address.model.display.sidepanel.PersonDisplay;
import seedu.address.model.display.sidepanel.SidePanelDisplay;
import seedu.address.model.display.sidepanel.SidePanelDisplayType;
import seedu.address.model.display.timeslots.FreeSchedule;
import seedu.address.model.display.timeslots.FreeTimeslot;
import seedu.address.model.display.timeslots.PersonSchedule;
import seedu.address.model.display.timeslots.PersonTimeslot;
import seedu.address.model.display.timeslots.WeekSchedule;
import seedu.address.model.group.Group;
import seedu.address.model.mapping.PersonToGroupMapping;
import seedu.address.model.mapping.Role;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;
import seedu.address.model.person.User;
import seedu.address.model.person.schedule.Event;
import seedu.address.model.person.schedule.Schedule;
import seedu.address.model.person.schedule.Timeslot;
import seedu.address.model.person.schedule.Venue;
import seedu.address.ui.util.ColorGenerator;

/**
 * Handler for all display models.
 */
public class ScheduleManager {

    private static final Logger logger = LogsCenter.getLogger(ScheduleManager.class);

    private static final int DAYS_OF_A_WEEK = 7;
    private static final int WEEKS_OF_A_MONTH = 4;

    private static final int FREE_TIMESLOT_TRHESHOLD = 15;
    private static final LocalTime SCHEDULE_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime SCHEDULE_END_TIME = LocalTime.of(20, 0);

    private GmapsModelManager gmapsModelManager;

    private SidePanelDisplay sidePanelDisplay;
    private ScheduleDisplay scheduleDisplay;

    public ScheduleManager(GmapsModelManager gmapsModelManager) {
        this.gmapsModelManager = gmapsModelManager;
    }

    /**
     * Returns the current state of the schedule's display.
     */
    public ScheduleState getState() {
        return scheduleDisplay.getState();
    }

    /**
     * Updates with a schedule of a person specified by name.
     */
    public void updateScheduleWithPerson(Person person, LocalDateTime time,
                                         ScheduleState type) {
        assert(type.equals(ScheduleState.PERSON));
        logger.log(Level.INFO, String.format("Generating Schedule of %s", person.getName().toString()));
        updateScheduleWindowDisplay(person, time, type);
    }

    /**
     * Updates with a schedule of the user.
     */
    public void updateScheduleWithUser(User user, LocalDateTime time, ScheduleState type) {
        assert(type.equals(ScheduleState.PERSON) || type.equals(ScheduleState.HOME));
        logger.log(Level.INFO, String.format("Generating Schedule of %s (user)", user.getName().toString()));
        updateScheduleWindowDisplay(user, time, type);
    }

    /**
     * Update with a schedule of a group.
     */
    public void updateScheduleWithGroup(Group group,
                                        ArrayList<Person> persons,
                                        ArrayList<PersonToGroupMapping> mappingList,
                                        LocalDateTime time,
                                        ScheduleState type) {

        assert(type.equals(ScheduleState.GROUP));
        mappingList.add(new PersonToGroupMapping(new PersonId(-1), group.getGroupId(), group.getUserRole()));
        GroupDisplay groupDisplay = new GroupDisplay(group);
        logger.log(Level.INFO, String.format("Generating Schedule of %s (group)",
                groupDisplay.getGroupName().toString()));
        updateScheduleWindowDisplay(persons, mappingList, groupDisplay, time, type);
    }

    /**
     * Update with a schedule of an ArrayList of Persons.
     */
    public void updateScheduleWithPersons(ArrayList<Person> persons,
                                          LocalDateTime now,
                                          ScheduleState type) {

        assert(type.equals(ScheduleState.GROUP));
        GroupDisplay groupDisplay = new GroupDisplay(persons);
        logger.log(Level.INFO, String.format("Generating Schedule of %s (group)",
                groupDisplay.getGroupName().toString()));
        updateScheduleWindowDisplay(persons, null, groupDisplay, now, type);

    }

    /**
     * Update to a Group schedule.
     */
    private void updateScheduleWindowDisplay(ArrayList<Person> persons,
                                             ArrayList<PersonToGroupMapping> mappings,
                                             GroupDisplay groupDisplay,
                                             LocalDateTime time,
                                             ScheduleState type) {
        assert (type.equals(ScheduleState.GROUP));

        ArrayList<FreeSchedule> freeSchedules = new ArrayList<>();
        ArrayList<PersonSchedule> personSchedules = new ArrayList<>();

        //Add all schedules.
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            Role role = Role.emptyRole();
            if (mappings != null) {
                for (PersonToGroupMapping map : mappings) {
                    if (map.getPersonId().equals(person.getPersonId())) {
                        role = map.getRole();
                        break;
                    }
                }
            } else {
                role = Role.emptyRole();
            }

            PersonSchedule personSchedule = generatePersonSchedule(
                    time,
                    person,
                    role,
                    ColorGenerator.generateColor(i),
                    true);
            personSchedules.add(personSchedule);
        }

        for (int week = 0; week < WEEKS_OF_A_MONTH; week++) {
            int finalWeek = week;
            FreeSchedule freeSchedule = generateFreeSchedule(personSchedules
                    .stream().map(schedule -> schedule.getScheduleDisplay().get(finalWeek))
                    .collect(Collectors.toCollection(ArrayList::new)), time);
            freeSchedules.add(freeSchedule);
        }

        GroupScheduleDisplay scheduleDisplay =
                new GroupScheduleDisplay(personSchedules, freeSchedules, groupDisplay);

        updateScheduleDisplay(scheduleDisplay);
    }

    /**
     * Update to a Person schedule.
     */
    private void updateScheduleWindowDisplay(Person person, LocalDateTime time, ScheduleState type) {
        assert (type.equals(ScheduleState.PERSON) || type.equals(ScheduleState.HOME));

        ArrayList<PersonSchedule> personSchedules = new ArrayList<>();
        PersonSchedule personSchedule = generatePersonSchedule(
                time,
                person,
                Role.emptyRole(),
                null,
                false);

        personSchedules.add(personSchedule);

        ScheduleDisplay scheduleDisplay;
        if (type == ScheduleState.PERSON) {
            scheduleDisplay = new PersonScheduleDisplay(personSchedules);
        } else {
            scheduleDisplay = new HomeScheduleDisplay(personSchedules);
        }
        updateScheduleDisplay(scheduleDisplay);
    }

    /**
     * Updates the scheduleDisplay.
     */
    private void updateScheduleDisplay(ScheduleDisplay scheduleDisplay) {
        this.scheduleDisplay = scheduleDisplay;
    }

    /**
     * Getter method to retrieve ScheduleDisplay.
     */
    public ScheduleDisplay getScheduleDisplay() {
        return scheduleDisplay;
    }

    /**
     * Getter method to retrieve side panel display.
     */
    public SidePanelDisplay getSidePanelDisplay() {
        return sidePanelDisplay;
    }

    /**
     * Generates the PersonSchedule of a Person.
     */
    private PersonSchedule generatePersonSchedule(LocalDateTime now,
                                                  Person person,
                                                  Role role,
                                                  String color,
                                                  boolean isInGroup) {

        ArrayList<WeekSchedule> scheduleDisplay = new ArrayList<>();
        for (int week = 0; week < WEEKS_OF_A_MONTH; week++) {
            scheduleDisplay.add(generateWeekSchedule(now.plusDays(week * DAYS_OF_A_WEEK), person, color, isInGroup));
        }
        return new PersonSchedule(new PersonDisplay(person, role), scheduleDisplay);
    }

    /**
     * Generates the WeekSchedule of a Person.
     */
    private WeekSchedule generateWeekSchedule(LocalDateTime date, Person person, String color, boolean isInGroup) {

        HashMap<DayOfWeek, ArrayList<PersonTimeslot>> scheduleDisplay = new HashMap<>();

        Schedule personSchedule = person.getSchedule();
        ArrayList<Event> events = personSchedule.getEvents();

        for (int day = 1; day <= DAYS_OF_A_WEEK; day++) {
            scheduleDisplay.put(DayOfWeek.of(day), new ArrayList<>());
        }

        for (int e = 0; e < events.size(); e++) {
            Event currentEvent = events.get(e);
            String eventName = currentEvent.getEventName();

            String selectedColor;
            if (isInGroup) {
                selectedColor = color;
            } else {
                selectedColor = ColorGenerator.generateColor(e);
            }

            ArrayList<Timeslot> timeslots = currentEvent.getTimeslots();
            for (int t = 0; t < timeslots.size(); t++) {
                Timeslot currentTimeslot = timeslots.get(t);

                LocalDateTime currentStartTime = currentTimeslot.getStartTime();
                LocalDateTime currentEndTime = currentTimeslot.getEndTime();
                Venue currentVenue = currentTimeslot.getVenue();

                //Checks to see if the currentStartTime is within the upcoming 7 days.
                if (date.toLocalDate().plusDays(DAYS_OF_A_WEEK).isAfter(currentStartTime.toLocalDate())
                        && date.toLocalDate().minusDays(1).isBefore(currentStartTime.toLocalDate())
                        && (SCHEDULE_START_TIME.isBefore(currentStartTime.toLocalTime())
                        || SCHEDULE_START_TIME.compareTo(currentStartTime.toLocalTime()) == 0)
                        && SCHEDULE_END_TIME.isAfter(currentStartTime.toLocalTime())) {

                    PersonTimeslot timeslot = new PersonTimeslot(
                            eventName,
                            currentStartTime.toLocalDate(),
                            currentStartTime.toLocalTime(),
                            currentEndTime.toLocalTime()
                                    .isAfter(SCHEDULE_END_TIME) ? SCHEDULE_END_TIME : currentEndTime.toLocalTime(),
                            currentVenue,
                            selectedColor,
                            isInGroup,
                            gmapsModelManager.closestLocationData(
                                    new ArrayList<>(List.of(currentVenue.getVenue())))
                    );

                    scheduleDisplay.get(currentStartTime.getDayOfWeek()).add(timeslot);
                } else {
                    logger.log(Level.FINE, String.format("%s is not within schedule range: "
                            + "event is omitted from schedule", eventName));
                }
            }
        }

        for (int i = 1; i <= DAYS_OF_A_WEEK; i++) {
            scheduleDisplay.get(DayOfWeek.of(i)).sort(
                    Comparator.comparing(PersonTimeslot::getStartTime));
        }


        return new WeekSchedule(scheduleDisplay);
    }

    /**
     * Generates a free schedule from a list of person schedules.
     */
    private FreeSchedule generateFreeSchedule(ArrayList<WeekSchedule> personSchedules,
                                              LocalDateTime now) {

        HashMap<DayOfWeek, ArrayList<FreeTimeslot>> freeSchedule = new HashMap<>();

        logger.log(Level.FINE, "Generating FreeSchedule..");

        // Used to allocate an ID for each FreeTimeslot
        int freeTimeslotIdCounter = 1;

        int currentDay = now.getDayOfWeek().getValue();
        for (int i = currentDay; i <= DAYS_OF_A_WEEK + currentDay - 1; i++) {

            int day = ((i - 1) % 7) + 1;
            freeSchedule.put(DayOfWeek.of(day), new ArrayList<>());

            LocalTime currentTime = SCHEDULE_START_TIME;
            ArrayList<String> lastVenues = new ArrayList<>();

            // initialize last venues to null for each person
            for (int j = 0; j < personSchedules.size(); j++) {
                lastVenues.add(null);
            }

            boolean isClash;
            LocalTime newFreeTimeslotStartTime = null;

            while (true) {

                isClash = false;
                ArrayList<String> currentLastVenues = new ArrayList<>(lastVenues);

                // loop through each person
                for (int j = 0; j < personSchedules.size(); j++) {
                    ArrayList<PersonTimeslot> timeslots = personSchedules.get(j).get(DayOfWeek.of(day));

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
                    if (newFreeTimeslotStartTime == null) {
                        newFreeTimeslotStartTime = currentTime;
                    }
                    lastVenues = new ArrayList<>(currentLastVenues);

                } else {
                    if (newFreeTimeslotStartTime != null) {

                        if (newFreeTimeslotStartTime.until(currentTime, MINUTES) >= FREE_TIMESLOT_TRHESHOLD) {
                            freeSchedule.get(DayOfWeek.of(day))
                                    .add(generateFreeTimeslot(
                                            freeTimeslotIdCounter,
                                            lastVenues,
                                            newFreeTimeslotStartTime,
                                            currentTime));

                            freeTimeslotIdCounter++;
                        }
                        newFreeTimeslotStartTime = null;
                    }
                    lastVenues = new ArrayList<>(currentLastVenues);
                }

                if (currentTime.equals(SCHEDULE_END_TIME)) {
                    if (!isClash) {
                        if (newFreeTimeslotStartTime.until(currentTime, MINUTES) >= FREE_TIMESLOT_TRHESHOLD) {
                            freeSchedule.get(DayOfWeek.of(day))
                                    .add(generateFreeTimeslot(
                                            freeTimeslotIdCounter,
                                            lastVenues,
                                            newFreeTimeslotStartTime,
                                            currentTime));
                            freeTimeslotIdCounter++;
                        }
                    }
                    break;
                }
                currentTime = currentTime.plusMinutes(1);
            }
        }
        return new FreeSchedule(freeSchedule);
    }

    /**
     * Helper method to generate a FreeTimeslot.
     */
    private FreeTimeslot generateFreeTimeslot(int id,
                                              ArrayList<String> lastVenues,
                                              LocalTime startTime,
                                              LocalTime endTime) {

        ArrayList<String> freeTimeslotLastVenues = new ArrayList<>();
        for (String string : lastVenues) {
            if (string != null) {
                freeTimeslotLastVenues.add(string);
            }
        }

        ClosestCommonLocationData closestCommonLocationData =
                gmapsModelManager.closestLocationData(freeTimeslotLastVenues);

        return new FreeTimeslot(
                id,
                freeTimeslotLastVenues,
                closestCommonLocationData,
                startTime,
                endTime);
    }

    /**
     * Updates the side panel display of type.
     */
    public void updateSidePanelDisplay(SidePanelDisplayType type,
                                       ArrayList<Person> persons, ArrayList<Group> groups) {

        SidePanelDisplay sidePanelDisplay;

        ArrayList<PersonDisplay> displayPersons = new ArrayList<>();
        ArrayList<GroupDisplay> displayGroups = new ArrayList<>();

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
     * Updates the side panel display.
     */
    public void updateSidePanelDisplay(SidePanelDisplay sidePanelDisplay) {
        this.sidePanelDisplay = sidePanelDisplay;
    }

}
