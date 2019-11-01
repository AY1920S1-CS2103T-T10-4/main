package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.display.schedulewindow.ScheduleWindowDisplayType;

/**
 * Command to export visual representations
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export-vr";

    public static final String MESSAGE_SUCCESS = "Exporting the current schedule showing...";
    public static final String MESSAGE_FAILURE = "No schedule to export! Please ensure that there is a schedule in "
            + "your schedule window display before you use the command!";
    public static final String MESSAGE_PERSON_NOT_FOUND = "This person does not exists in the address book!";
    public static final String MESSAGE_GROUP_NOT_FOUND = "This group does not exists in the address book!";
    public static final String MESSAGE_USAGE = "Export command does not take in any arguments!";


    public ExportCommand() {
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        //Does nothing to the model.
        requireNonNull(model);
        ScheduleWindowDisplayType state = model.getState();
        if (!state.equals(ScheduleWindowDisplayType.PERSON) && !state.equals(ScheduleWindowDisplayType.GROUP)) {
            throw new CommandException(MESSAGE_FAILURE);
        }
        return new CommandResult(MESSAGE_SUCCESS, false , false, true);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ExportCommand;
    }

    @Override
    public boolean equals(Command o) {
        return o instanceof ExportCommand;
    }
}
