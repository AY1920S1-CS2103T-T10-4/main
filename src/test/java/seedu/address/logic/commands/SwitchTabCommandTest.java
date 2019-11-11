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

public class SwitchTabCommandTest {

    private ModelManager model;

    @BeforeEach
    void init() {
        model = TypicalModel.generateModelWithSchedules();
    }

    @Test
    void failureWhenGroupScheduleIsShown() throws GroupNotFoundException {
        model.updateScheduleWithGroup(
                GROUP_NAME1, LocalDateTime.now(), ScheduleState.GROUP);
        assertThrows(CommandException.class, () -> new SwitchTabCommand().execute(model));
    }

    @Test
    void failureWhenPersonScheduleIsShown() throws PersonNotFoundException {
        model.updateScheduleWithPerson(
                TypicalPersons.ALICE.getName(), LocalDateTime.now(), ScheduleState.PERSON);
        assertThrows(CommandException.class, () -> new SwitchTabCommand().execute(model));
    }

    @Test
    void successWhenHomePageIsShown() throws CommandException {
        CommandResult actualResult = new SwitchTabCommand().execute(model);
        CommandResult expectedResult = new CommandResultBuilder(SwitchTabCommand.MESSAGE_SUCCESS).setSwitchTabs()
                .build();
        assertEquals(expectedResult, actualResult);
    }
}
