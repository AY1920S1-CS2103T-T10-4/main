package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.time.LocalDateTime;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.display.schedulewindow.ScheduleWindowDisplayType;
import seedu.address.model.display.sidepanel.SidePanelDisplayType;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonDescriptor;

/**
 * Adds a person.
 */
public class AddPersonCommand extends Command {

    public static final String COMMAND_WORD = "addperson";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + PREFIX_NAME + " NAME";

    public static final String MESSAGE_SUCCESS = "New person added: ";
    public static final String MESSAGE_FAILURE = "Unable to add person: ";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    private final PersonDescriptor personDescriptor;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddPersonCommand(PersonDescriptor personDescriptor) {
        requireNonNull(personDescriptor);
        this.personDescriptor = personDescriptor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Name name = personDescriptor.getName();
        if (model.findPerson(name) != null) {
            return new CommandResult(MESSAGE_FAILURE + MESSAGE_DUPLICATE_PERSON);
        } else {
            Person person = model.addPerson(personDescriptor);
            if (person != null) {

                // update main window
                model.updateDetailWindowDisplay(person.getName(), LocalDateTime.now(),
                        ScheduleWindowDisplayType.PERSON);

                // update side panel
                model.updateSidePanelDisplay(SidePanelDisplayType.PERSON);

                return new CommandResult(MESSAGE_SUCCESS + person.details());
            } else {
                return new CommandResult(MESSAGE_FAILURE + MESSAGE_DUPLICATE_PERSON);
            }
        }
    }

    @Override
    public boolean equals(Command command) {
        return command == this // short circuit if same object
                || (command instanceof AddPersonCommand // instanceof handles nulls
                && personDescriptor.equals(((AddPersonCommand) command).personDescriptor));
    }

}
