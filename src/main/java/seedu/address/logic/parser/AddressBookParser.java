package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.commands.AddGroupCommand;
import seedu.address.logic.commands.AddPersonCommand;
import seedu.address.logic.commands.AddToGroupCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteGroupCommand;
import seedu.address.logic.commands.DeletePersonCommand;
import seedu.address.logic.commands.EditGroupCommand;
import seedu.address.logic.commands.EditPersonCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindGroupCommand;
import seedu.address.logic.commands.FindPersonCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.NusmodCommand;

import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddPersonCommand.COMMAND_WORD:
            return new AddPersonCommandParser().parse(arguments);

        case EditPersonCommand.COMMAND_WORD:
            return new EditPersonCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();
        case RemarkCommand.COMMAND_WORD:
            return new RemarkCommandParser().parse(arguments);

        case NusmodCommand.COMMAND_WORD:
            return new NusmodCommandParser().parse(arguments);

        case AddGroupCommand.COMMAND_WORD:
            return new AddGroupCommandParser().parse(arguments);

        case AddEventCommand.COMMAND_WORD:
            return new AddEventCommandParser().parse(arguments);

        case AddToGroupCommand.COMMAND_WORD:
            return new AddToGroupCommandParser().parse(arguments);

        case DeletePersonCommand.COMMAND_WORD:
            return new DeletePersonCommandParser().parse(arguments);

        case DeleteGroupCommand.COMMAND_WORD:
            return new DeleteGroupCommandParser().parse(arguments);

        case FindPersonCommand.COMMAND_WORD:
            return new FindPersonCommandParser().parse(arguments);

        case FindGroupCommand.COMMAND_WORD:
            return new FindGroupCommandParser().parse(arguments);

        case EditGroupCommand.COMMAND_WORD:
            return new EditGroupCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
