package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.display.detailwindow.DetailWindowDisplay;
import seedu.address.model.display.detailwindow.DetailWindowDisplayType;

public class ToggleNextWeekCommand extends Command {

    public static final String COMMAND_WORD = "nw";
    public static final String MESSAGE_SUCCESS = "Showing next week's schedule";
    public static final String MESSAGE_USAGE = "To view next week's schedule, type nw!";

    public ToggleNextWeekCommand() {

    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        model.updateDetailWindowDisplay(new DetailWindowDisplay(DetailWindowDisplayType.NONE));
        return new CommandResult(MESSAGE_SUCCESS, false, false, false, false, false, true);
    }

    @Override
    public boolean equals(Command command) {
        return command instanceof ToggleNextWeekCommand;
    }
}
