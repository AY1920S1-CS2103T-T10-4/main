package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_FREETIMESLOT_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUPNAME;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.display.detailwindow.ClosestCommonLocationData;
import seedu.address.model.display.schedulewindow.FreeTimeslot;
import seedu.address.model.display.schedulewindow.ScheduleWindowDisplayType;
import seedu.address.model.display.sidepanel.SidePanelDisplayType;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.group.exceptions.GroupNotFoundException;

/**
 * Command to show popup of the locations suggested.
 */
public class PopupCommand extends Command {

    public static final String COMMAND_WORD = "popup";
    public static final String MESSAGE_SUCCESS = "Closest Location found! See the popup for information.";
    public static final String MESSAGE_INTERNAL_ERROR = "Internal error";
    public static final String MESSAGE_USER_ERROR = "We could not find a common "
            + "location because:\n";
    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + PREFIX_GROUPNAME + " GROUPNAME"
            + PREFIX_FREETIMESLOT_ID + " FREETIMESLOTID";
    private final Logger logger = LogsCenter.getLogger(this.getClass());
    private GroupName groupName;
    private int id;

    public PopupCommand(GroupName groupName, int id) {
        this.groupName = groupName;
        this.id = id;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {

        try {
            Group group = model.findGroup(groupName);

            // update main window
            model.updateScheduleWindowDisplay(group.getGroupName(), LocalDateTime.now(),
                    ScheduleWindowDisplayType.GROUP);

            //update side panel display
            model.updateSidePanelDisplay(SidePanelDisplayType.GROUP);

            //Get Week 0 FreeSchedule and find the correct FreeTimeslot.
            Optional<FreeTimeslot> freeTimeslot =
                    model.getScheduleWindowDisplay().getFreeSchedule().get(0).getFreeTimeslot(id);

            System.out.println("TEST: " + freeTimeslot.get().toString());

            ClosestCommonLocationData commonLocationData;
            if (freeTimeslot.isPresent()) {
                commonLocationData = freeTimeslot.get().getClosestCommonLocationData();
            } else {
                return new CommandResult("Invalid time slot ID: " + id + ". Please enter a valid id as "
                        + "shown in the GUI.");
            }

            if (!commonLocationData.isOk()) {
                String errorResponse = "";

                if (freeTimeslot.get().getVenues().size() == 0) {
                    errorResponse = "Everyone has not started their schedule yet. Feel free to meet up any time.";
                } else if (commonLocationData.getErrorResponse().length() != 0) {
                    errorResponse = commonLocationData.getErrorResponse();
                } else {
                    logger.warning("Unknown error for time slot: " + freeTimeslot.get().toString());
                    return new CommandResult(MESSAGE_INTERNAL_ERROR);
                }

                return new CommandResult(MESSAGE_USER_ERROR + errorResponse);
            }

            if (freeTimeslot.isEmpty()) {
                return new CommandResult(MESSAGE_INTERNAL_ERROR);
            }

            return new CommandResult(MESSAGE_SUCCESS, false, false, false, false, true,
                    freeTimeslot.get().getClosestCommonLocationData());
        } catch (GroupNotFoundException e) {
            return new CommandResult(MESSAGE_INTERNAL_ERROR);
        }
    }

    @Override
    public boolean equals(Command command) {
        return (command instanceof PopupCommand);
    }
}
