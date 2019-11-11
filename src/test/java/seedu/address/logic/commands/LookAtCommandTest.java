package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.grouputil.TypicalGroups.GROUP_NAME1;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.display.scheduledisplay.ScheduleState;
import seedu.address.model.group.exceptions.GroupNotFoundException;
import seedu.address.testutil.TypicalPersons;
import seedu.address.testutil.modelutil.TypicalModel;

public class LookAtCommandTest {

    private ModelManager model;

    @BeforeEach
    void init() {
        model = TypicalModel.generateModelWithSchedules();
    }

    @Test
    void lookAtCommandSuccessWithAllInputs() throws GroupNotFoundException, CommandException {
        model.updateScheduleWithGroup(
                GROUP_NAME1, LocalDateTime.now(), ScheduleState.GROUP);
        assertEquals(model.getState(), ScheduleState.GROUP);

        //Group that contains this person.
        CommandResult actualCommandResult = new LookAtGroupMemberCommand(List.of(TypicalPersons.ALICE.getName()))
                .execute(model);
        CommandResult expectedResult = new CommandResultBuilder(String.format(LookAtGroupMemberCommand.MESSAGE_SUCCESS,
                TypicalPersons.ALICE.getName())).setFilter().build();
        assertEquals(expectedResult, actualCommandResult);
    }

    @Test
    void lookAtCommandSuccessWithSomeInvalidInputs() throws GroupNotFoundException, CommandException {
        model.updateScheduleWithGroup(
                GROUP_NAME1, LocalDateTime.now(), ScheduleState.GROUP);
        assertEquals(model.getState(), ScheduleState.GROUP);

        //Group that contains this person.
        CommandResult actualCommandResult = new LookAtGroupMemberCommand(List.of(TypicalPersons.ALICE.getName(),
                TypicalPersons.DANIEL.getName())).execute(model);
        String expectedMessage = String.format(LookAtGroupMemberCommand.MESSAGE_SUCCESS,
                TypicalPersons.ALICE.getName().fullName) + "\n"
                + String.format(LookAtGroupMemberCommand.MESSAGE_NOT_FOUND, TypicalPersons.DANIEL.getName().fullName);;
        CommandResult expectedResult = new CommandResultBuilder(expectedMessage).setFilter().build();
        assertEquals(expectedResult, actualCommandResult);
    }

    @Test
    void noInputTest() throws GroupNotFoundException, CommandException {
        model.updateScheduleWithGroup(
                GROUP_NAME1, LocalDateTime.now(), ScheduleState.GROUP);
        assertEquals(model.getState(), ScheduleState.GROUP);

        //Group that contains this person.
        CommandResult actualCommandResult = new LookAtGroupMemberCommand(new ArrayList<>()).execute(model);
        CommandResult expectedResult = new CommandResultBuilder(String.format(LookAtGroupMemberCommand.MESSAGE_SUCCESS,
                "")).setFilter().build();
        assertEquals(expectedResult, actualCommandResult);
    }
}
