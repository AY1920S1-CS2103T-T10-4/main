package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.grouputil.TypicalGroups.GROUP_NAME1;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.display.scheduledisplay.ScheduleState;
import seedu.address.model.group.exceptions.GroupNotFoundException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.TypicalPersons;
import seedu.address.testutil.modelutil.TypicalModel;

public class ToggleNextCommandTest {
    private ModelManager model;

    @BeforeEach
    void init() {
        model = TypicalModel.generateModelWithSchedules();
    }

    @Test
    void successWhenGroupScheduleIsShown() throws GroupNotFoundException, CommandException {
        model.updateScheduleWithGroup(GROUP_NAME1, LocalDateTime.now(), ScheduleState.GROUP);
        CommandResult actualResult = new ToggleNextWeekCommand().execute(model);
        CommandResult expectedResult = new CommandResultBuilder(ToggleNextWeekCommand.MESSAGE_SUCCESS)
                .setToggleNextWeek().build();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void successWhenPersonScheduleIsShown() throws PersonNotFoundException, CommandException {
        model.updateScheduleWithPerson(
                TypicalPersons.ALICE.getName(), LocalDateTime.now(), ScheduleState.PERSON);
        CommandResult actualResult = new ToggleNextWeekCommand().execute(model);
        CommandResult expectedResult = new CommandResultBuilder(ToggleNextWeekCommand.MESSAGE_SUCCESS)
                .setToggleNextWeek().build();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void failureWhenHomePageIsShown() {
        assertThrows(CommandException.class, () -> new ToggleNextWeekCommand().execute(model));
    }
}
