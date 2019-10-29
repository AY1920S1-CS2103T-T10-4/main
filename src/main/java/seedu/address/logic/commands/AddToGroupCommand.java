package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUPNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.time.LocalDateTime;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.display.schedulewindow.ScheduleWindowDisplayType;
import seedu.address.model.display.sidepanel.SidePanelDisplayType;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.mapping.PersonToGroupMapping;
import seedu.address.model.mapping.Role;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Adds a person into a group.
 */
public class AddToGroupCommand extends Command {
    public static final String COMMAND_WORD = "addtogroup";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + PREFIX_NAME + " NAME "
            + PREFIX_GROUPNAME + " GROUPNAME";

    public static final String MESSAGE_SUCCESS = "Add to group success: ";
    public static final String MESSAGE_FAILURE = "Unable to find person or group";
    public static final String MESSAGE_DUPLICATE = "Duplicate Mapping";

    public final Name name;
    public final GroupName groupName;
    public final Role role;

    public AddToGroupCommand(Name name, GroupName groupName, Role role) {
        this.name = name;
        this.groupName = groupName;
        this.role = role;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {

        if (name == null || groupName == null) {
            return new CommandResult(MESSAGE_FAILURE);
        } else {
            Person person = model.findPerson(name);
            Group group = model.findGroup(groupName);

            PersonToGroupMapping mapping = new PersonToGroupMapping(person.getPersonId(), group.getGroupId(), role);

            if (model.addPersonToGroupMapping(mapping)) {

                // updates main window
                model.updateDetailWindowDisplay(group.getGroupName(),
                        LocalDateTime.now(), ScheduleWindowDisplayType.GROUP);

                // updates side panel
                model.updateSidePanelDisplay(SidePanelDisplayType.GROUP);

                return new CommandResult(MESSAGE_SUCCESS + mapping.toString());
            } else {
                return new CommandResult(MESSAGE_DUPLICATE);
            }
        }
    }

    @Override
    public boolean equals(Command command) {
        if (command == null) {
            return false;
        } else if (!(command instanceof AddToGroupCommand)) {
            return false;
        } else if (((AddToGroupCommand) command).name.equals(this.name)
                && ((AddToGroupCommand) command).groupName.equals(this.groupName)) {
            return true;
        } else {
            return false;
        }
    }
}
