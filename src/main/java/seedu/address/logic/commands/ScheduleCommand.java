package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUPNAME;

import java.time.LocalDateTime;
import java.util.ArrayList;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.display.detailwindow.DetailWindowDisplayType;
import seedu.address.model.display.detailwindow.WeekSchedule;
import seedu.address.model.display.sidepanel.SidePanelDisplayType;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;

/**
 * Gives the schedule for the week of a group.
 */
public class ScheduleCommand extends Command {

    public static final String COMMAND_WORD = "schedule";
    public static final String MESSAGE_SUCCESS = "Schedule found: \n\n";
    public static final String MESSAGE_FAILURE = "Unable to generate schedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + PREFIX_GROUPNAME + " GROUPNAME";

    public final GroupName groupName;

    public ScheduleCommand(GroupName groupName) {
        this.groupName = groupName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Group group = model.findGroup(groupName);
        if (group == null) {
            return new CommandResult(MESSAGE_FAILURE);
        }

        // update main window
        model.updateDetailWindowDisplay(group.getGroupName(), LocalDateTime.now(), DetailWindowDisplayType.NONE);

        // update side panel
        model.updateSidePanelDisplay(SidePanelDisplayType.GROUPS);

        ArrayList<WeekSchedule> schedules = model.getDetailWindowDisplay().getWeekSchedules();
        String output = "";
        for (WeekSchedule s : schedules) {
            output += s.toString() + "\n";
        }
        return new CommandResult(MESSAGE_SUCCESS + output);
    }

    @Override
    public boolean equals(Command command) {
        if (command == null) {
            return false;
        } else if (!(command instanceof ScheduleCommand)) {
            return false;
        } else if (((ScheduleCommand) command).groupName.equals(this.groupName)) {
            return true;
        } else {
            return false;
        }
    }
}
