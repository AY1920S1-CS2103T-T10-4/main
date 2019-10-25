package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.Optional;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.display.detailwindow.DetailWindowDisplayType;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/** Command to export visual representations */
public class ExportCommand<T> extends Command {

    public static final String COMMAND_WORD = "export-vr";

    public static final String MESSAGE_SUCCESS = "Exporting %1$s";
    public static final String MESSAGE_FAILURE = "Failed to export...";
    public static final String MESSAGE_PERSON_NOT_FOUND = "This person does not exists in the address book!";
    public static final String MESSAGE_GROUP_NOT_FOUND = "This group does not exists in the address book!";
    public static final String MESSAGE_USAGE = "Export command takes in a person's or group's name as argument!";

    private T name;

    public ExportCommand(T name) {
        requireNonNull(name);
        this.name = name;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        //Does nothing to the model.
        requireNonNull(model);
        if (name instanceof Name) {
            ObservableList<Person> personList = model.getObservablePersonList();

            Optional<Person> person = Optional.empty();
            for (Person p : personList) {
                if (p.getName().equals(name)) {
                    person = Optional.of(p);
                    break;
                }
            }

            if (person.isEmpty()) {
                throw new CommandException(MESSAGE_PERSON_NOT_FOUND);
            }

            model.updateDetailWindowDisplay((Name) name, LocalDateTime.now(), DetailWindowDisplayType.PERSON);
            return new CommandResult(String.format(MESSAGE_SUCCESS, person.get()), false,
                    false, true);
        } else {
            ObservableList<Group> groupList = model.getObservableGroupList();
            Optional<Group> group = Optional.empty();
            for (Group g : groupList) {
                if (g.getGroupName().equals((GroupName) name)) {
                    group = Optional.of(g);
                    break;
                }
            }

            if (group.isEmpty()) {
                throw new CommandException(MESSAGE_GROUP_NOT_FOUND);
            }

            model.updateDetailWindowDisplay((GroupName) name, LocalDateTime.now(), DetailWindowDisplayType.GROUP);
            return new CommandResult(String.format(MESSAGE_SUCCESS, group.get()), false, false,
                    true);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof ExportCommand) {
            return ((ExportCommand) o).name.equals(this.name);
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Command o) {
        if (o == this) {
            return true;
        } else if (o instanceof ExportCommand) {
            return ((ExportCommand) o).name.equals(this.name);
        } else {
            return false;
        }
    }
}
