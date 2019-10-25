package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.display.detailwindow.DetailWindowDisplay;
import seedu.address.model.display.detailwindow.DetailWindowDisplayType;

/**
 * Command to handle scrolling events using CLI.
 */
public class ScrollCommand extends Command {

    public static final String COMMAND_WORD = "n";
    public static final String MESSAGE_SUCCESS = "Showing next view of schedule";

    public ScrollCommand() {
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        model.updateDetailWindowDisplay(new DetailWindowDisplay(DetailWindowDisplayType.NONE));
        return new CommandResult(MESSAGE_SUCCESS, false, false, false, true);
    }

    @Override
    public boolean equals(Command command) {
        return (command instanceof ScrollCommand);
    }
}
