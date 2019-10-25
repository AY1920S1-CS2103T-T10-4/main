package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.commands.AddGroupCommand;
import seedu.address.logic.commands.AddNusModCommand;
import seedu.address.logic.commands.AddNusModsCommand;
import seedu.address.logic.commands.AddPersonCommand;
import seedu.address.logic.commands.AddToGroupCommand;
import seedu.address.logic.commands.ClosestLocationCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteGroupCommand;
import seedu.address.logic.commands.DeletePersonCommand;
import seedu.address.logic.commands.EditGroupCommand;
import seedu.address.logic.commands.EditPersonCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.FindGroupCommand;
import seedu.address.logic.commands.FindPersonCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.PopupCommand;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.logic.commands.ScrollCommand;
import seedu.address.logic.commands.ShowCommand;
import seedu.address.logic.commands.ShowNusModCommand;
import seedu.address.logic.commands.SwitchTabCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {
    /**
     * Represents the two parts of a command: its {@code commandWord} and {@code arguments}.
     */
    public static class CommandTokens {
        public final String commandWord;
        public final String arguments;

        public CommandTokens(String commandWord, String arguments) {
            this.commandWord = commandWord;
            this.arguments = arguments;
        }

        public int getCommandWordLength() {
            return commandWord.length();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CommandTokens that = (CommandTokens) o;
            return commandWord.equals(that.commandWord)
                    && arguments.equals(that.arguments);
        }

        @Override
        public int hashCode() {
            return Objects.hash(commandWord, arguments);
        }
    }

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
        final CommandTokens commandTokens = tokenizeCommand(userInput);
        final String commandWord = commandTokens.commandWord;
        final String arguments = commandTokens.arguments;
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

        case ShowNusModCommand.COMMAND_WORD:
            return new ShowNusModCommandParser().parse(arguments);

        case AddNusModCommand.COMMAND_WORD:
            return new AddNusModCommandParser().parse(arguments);

        case AddNusModsCommand.COMMAND_WORD:
            return new AddNusModsCommandParser().parse(arguments);

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

        case ShowCommand.COMMAND_WORD:
            return new ShowCommandParser().parse(arguments);

        case ExportCommand.COMMAND_WORD:
            return new ExportCommandParser().parse(arguments);

        case ClosestLocationCommand.COMMAND_WORD:
            return new ClosestLocationCommandParser().parse(arguments);

        case ScheduleCommand.COMMAND_WORD:
            return new ScheduleCommandParser().parse(arguments);

        case SwitchTabCommand.COMMAND_WORD:
            return new SwitchTabCommand();

        case ScrollCommand.COMMAND_WORD:
            return new ScrollCommand();

        case PopupCommand.COMMAND_WORD:
            return new PopupCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Tokenize user input into two parts: {@code commandWord} and {@code arguments}.
     *
     * @param userInput Full user input string.
     * @return The {@code userInput} split into the two {@code commandWord} and {@code arguments} parts.
     * @throws ParseException if the user input does not conform the expected format
     */
    public static CommandTokens tokenizeCommand(final String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        return new CommandTokens(
                matcher.group("commandWord"),
                matcher.group("arguments")
        );
    }
}
