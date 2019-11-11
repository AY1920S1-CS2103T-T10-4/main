package seedu.address.model.display.scheduledisplay;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.grouputil.TypicalGroups.GROUP_DESCRIPTION1;
import static seedu.address.testutil.grouputil.TypicalGroups.GROUP_NAME1;
import static seedu.address.testutil.personutil.TypicalPersonDescriptor.ALICE;
import static seedu.address.testutil.personutil.TypicalPersonDescriptor.ZACK;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.ModelManager;
import seedu.address.model.display.sidepanel.GroupDisplay;
import seedu.address.model.display.timeslots.FreeSchedule;
import seedu.address.model.group.exceptions.GroupNotFoundException;
import seedu.address.model.person.exceptions.InvalidTimeslotException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.modelutil.TypicalModel;

class GroupScheduleDisplayTest {

    private ModelManager modelManager;
    private GroupScheduleDisplay groupScheduleDisplay;

    @BeforeEach
    void init() throws GroupNotFoundException {
        modelManager = TypicalModel.generateModelWithSchedules();
        modelManager.updateScheduleWithGroup(GROUP_NAME1, LocalDateTime.now(), ScheduleState.GROUP);
        groupScheduleDisplay = (GroupScheduleDisplay) modelManager.getScheduleDisplay();
    }

    @Test
    void getGroupDisplay() {
        GroupDisplay groupDisplay = groupScheduleDisplay.getGroupDisplay();
        assertEquals(groupDisplay.getGroupDescription(), GROUP_DESCRIPTION1);
        assertEquals(groupDisplay.getGroupName(), GROUP_NAME1);
    }

    @Test
    void getFreeSchedule() {
        ArrayList<FreeSchedule> freeSchedules = groupScheduleDisplay.getFreeSchedule();
        assertTrue(freeSchedules.size() == 4);
    }

    @Test
    void getFreeTimeslot() {
        assertThrows(InvalidTimeslotException.class, () ->
                groupScheduleDisplay.getFreeTimeslot(1, -1));

        assertThrows(InvalidTimeslotException.class, () ->
                groupScheduleDisplay.getFreeTimeslot(1, 123));

        assertDoesNotThrow(() ->
                groupScheduleDisplay.getFreeTimeslot(1, 1));

        assertDoesNotThrow(() ->
                groupScheduleDisplay.getFreeTimeslot(2, 1));
    }

    @Test
    void getState() {
        assertEquals(groupScheduleDisplay.getState(), ScheduleState.GROUP);
    }

    @Test
    void getPersonTimeslot() {
        assertThrows(PersonNotFoundException.class, () ->
                groupScheduleDisplay.getPersonTimeslot(ZACK.getName(), 0, 1));
        assertDoesNotThrow(() ->
                groupScheduleDisplay.getPersonTimeslot(ALICE.getName(), 0, 1));
    }

    @Test
    void getPersonDisplays() {
        assertTrue(groupScheduleDisplay.getPersonDisplays().size() == 3);
    }

    @Test
    void getPersonSchedules() {
        assertEquals(groupScheduleDisplay.getPersonSchedules().size(), 3);
    }
}
