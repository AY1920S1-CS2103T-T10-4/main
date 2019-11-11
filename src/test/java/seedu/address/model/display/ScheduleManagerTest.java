package seedu.address.model.display;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.grouputil.TypicalGroups.GROUP_NAME1;
import static seedu.address.testutil.personutil.TypicalPersonDescriptor.ALICE;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.GmapsModelManager;
import seedu.address.model.TimeBook;
import seedu.address.model.display.scheduledisplay.ScheduleState;
import seedu.address.model.display.sidepanel.SidePanelDisplayType;
import seedu.address.model.group.Group;
import seedu.address.model.group.exceptions.GroupNotFoundException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.modelutil.TypicalModel;

class ScheduleManagerTest {

    private ScheduleManager scheduleManager;
    private TimeBook timeBook;

    @BeforeEach
    void init() {
        timeBook = TypicalModel.generateTypicalModel().getTimeBook();
        scheduleManager = new ScheduleManager(new GmapsModelManager());
    }

    @Test
    void getState_home() {
        scheduleManager.updateScheduleWithUser(timeBook.getPersonList().getUser(),
                LocalDateTime.now(),
                ScheduleState.HOME);

        assertEquals(ScheduleState.HOME, scheduleManager.getState());
    }

    @Test
    void getState_person() throws PersonNotFoundException {
        scheduleManager.updateScheduleWithUser(timeBook.getPersonList().getUser(),
                LocalDateTime.now(), ScheduleState.PERSON);

        assertEquals(ScheduleState.PERSON, scheduleManager.getState());

        scheduleManager.updateScheduleWithPerson(timeBook.getPersonList().findPerson(ALICE.getName()),
                LocalDateTime.now(),
                ScheduleState.PERSON);

        assertEquals(ScheduleState.PERSON, scheduleManager.getState());
    }

    @Test
    void getState_group() throws GroupNotFoundException {
        Group group = timeBook.getGroupList().findGroup(GROUP_NAME1);
        scheduleManager.updateScheduleWithGroup(group,
                timeBook.getPersonsOfGroup(GROUP_NAME1),
                timeBook.getPersonToGroupMappingList().getMappingsOfGroup(group.getGroupId()),
                LocalDateTime.now(),
                ScheduleState.GROUP);

        assertEquals(ScheduleState.GROUP, scheduleManager.getState());

        ArrayList<Person> persons = timeBook.getPersonList().getPersons();
        scheduleManager.updateScheduleWithPersons(
                persons, LocalDateTime.now(),
                ScheduleState.GROUP);

        assertEquals(ScheduleState.GROUP, scheduleManager.getState());
    }

    @Test
    void updateDisplayWithPerson() {
        assertDoesNotThrow(() ->
                scheduleManager.updateScheduleWithPerson(
                        timeBook.getPersonList().findPerson(ALICE.getName()),
                        LocalDateTime.now(),
                        ScheduleState.PERSON));
    }

    @Test
    void updateDisplayWithUser() {
        assertDoesNotThrow(() ->
                scheduleManager.updateScheduleWithUser(
                        timeBook.getPersonList().getUser(),
                        LocalDateTime.now(),
                        ScheduleState.PERSON));

        assertEquals(scheduleManager.getState(), ScheduleState.PERSON);

        assertDoesNotThrow(() ->
                scheduleManager.updateScheduleWithUser(
                        timeBook.getPersonList().getUser(),
                        LocalDateTime.now(),
                        ScheduleState.HOME));

        assertEquals(scheduleManager.getState(), ScheduleState.HOME);

    }

    @Test
    void updateDisplayWithGroup() throws GroupNotFoundException {
        Group group = timeBook.getGroupList().findGroup(GROUP_NAME1);
        assertDoesNotThrow(() ->
                scheduleManager.updateScheduleWithGroup(group,
                        timeBook.getPersonsOfGroup(GROUP_NAME1),
                        timeBook.getPersonToGroupMappingList().getMappingsOfGroup(group.getGroupId()),
                        LocalDateTime.now(),
                        ScheduleState.GROUP));
    }

    @Test
    void updateDisplayWithPersons() {
        ArrayList<Person> persons = timeBook.getPersonList().getPersons();
        assertDoesNotThrow(() ->
                scheduleManager.updateScheduleWithPersons(
                        persons, LocalDateTime.now(),
                        ScheduleState.GROUP));

    }

    @Test
    void updateSidePanelDisplay_person() {
        scheduleManager.updateSidePanelDisplay(SidePanelDisplayType.PERSON,
                timeBook.getPersonList().getPersons(),
                timeBook.getGroupList().getGroups());

        assertEquals(SidePanelDisplayType.PERSON,
                scheduleManager.getSidePanelDisplay().getSidePanelDisplayType());
    }

    @Test
    void updateSidePanelDisplay_group() {
        scheduleManager.updateSidePanelDisplay(SidePanelDisplayType.GROUP,
                timeBook.getPersonList().getPersons(),
                timeBook.getGroupList().getGroups());

        assertEquals(SidePanelDisplayType.GROUP,
                scheduleManager.getSidePanelDisplay().getSidePanelDisplayType());
    }

    @Test
    void getScheduleDisplay() {
        scheduleManager.updateScheduleWithUser(
                timeBook.getPersonList().getUser(),
                LocalDateTime.now(),
                ScheduleState.PERSON);

        assertEquals(scheduleManager.getScheduleDisplay().getState(), ScheduleState.PERSON);
    }

    @Test
    void getSidePanelDisplay() {

        scheduleManager.updateSidePanelDisplay(SidePanelDisplayType.PERSON,
                timeBook.getPersonList().getPersons(),
                timeBook.getGroupList().getGroups());

        assertEquals(scheduleManager.getSidePanelDisplay().getSidePanelDisplayType(), SidePanelDisplayType.PERSON);

        scheduleManager.updateSidePanelDisplay(SidePanelDisplayType.GROUP,
                timeBook.getPersonList().getPersons(),
                timeBook.getGroupList().getGroups());

        assertEquals(scheduleManager.getSidePanelDisplay().getSidePanelDisplayType(), SidePanelDisplayType.GROUP);
    }
}
