package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.commands.AddGroupCommand;
import seedu.address.logic.commands.AddNusModCommand;
import seedu.address.logic.commands.AddNusModsCommand;
import seedu.address.logic.commands.AddPersonCommand;
import seedu.address.logic.commands.AddToGroupCommand;
import seedu.address.logic.commands.ClosestLocationCommand;
import seedu.address.logic.commands.DeleteEventCommand;
import seedu.address.logic.commands.DeleteFromGroupCommand;
import seedu.address.logic.commands.DeleteGroupCommand;
import seedu.address.logic.commands.DeletePersonCommand;
import seedu.address.logic.commands.EditGroupCommand;
import seedu.address.logic.commands.EditPersonCommand;
import seedu.address.logic.commands.EditUserCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.FindGroupCommand;
import seedu.address.logic.commands.FindPersonCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HomeCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.PopupCommand;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.logic.commands.ScrollCommand;
import seedu.address.logic.commands.ShowCommand;
import seedu.address.logic.commands.ShowNusModCommand;
import seedu.address.logic.commands.SwitchTabCommand;
import seedu.address.logic.commands.ToggleNextWeekCommand;

/**
 * Contains utility methods used for suggesting user commands in the *SuggestingCommandBox classes.
 */
public class SuggestingCommandUtil {
    private static ObservableList<String> commandWords = FXCollections.observableArrayList(
            AddEventCommand.COMMAND_WORD,
            AddGroupCommand.COMMAND_WORD,
            AddNusModCommand.COMMAND_WORD,
            AddNusModsCommand.COMMAND_WORD,
            AddPersonCommand.COMMAND_WORD,
            AddToGroupCommand.COMMAND_WORD,
            ClosestLocationCommand.COMMAND_WORD,
            DeleteEventCommand.COMMAND_WORD,
            DeleteFromGroupCommand.COMMAND_WORD,
            DeleteGroupCommand.COMMAND_WORD,
            DeletePersonCommand.COMMAND_WORD,
            EditGroupCommand.COMMAND_WORD,
            EditPersonCommand.COMMAND_WORD,
            EditUserCommand.COMMAND_WORD,
            ExitCommand.COMMAND_WORD,
            ExportCommand.COMMAND_WORD,
            FindGroupCommand.COMMAND_WORD,
            FindPersonCommand.COMMAND_WORD,
            HelpCommand.COMMAND_WORD,
            HomeCommand.COMMAND_WORD,
            ListCommand.COMMAND_WORD,
            PopupCommand.COMMAND_WORD,
            ScheduleCommand.COMMAND_WORD,
            ScrollCommand.COMMAND_WORD,
            ShowCommand.COMMAND_WORD,
            ShowNusModCommand.COMMAND_WORD,
            SwitchTabCommand.COMMAND_WORD,
            ToggleNextWeekCommand.COMMAND_WORD
    );
    private static ObservableList<String> readOnlyCommandWords = FXCollections.unmodifiableObservableList(commandWords);

    public static ObservableList<String> getCommandWords() {
        return readOnlyCommandWords;
    }

    /**
     * Creates a {@link Predicate} that checks if the candidate string contains the {@code input} characters
     * in the same order while allowing for any printable characters in between and after.
     * <p>
     * For example, calling {@code createOrderedMatcher("mdm")} creates a {@link Predicate} where strings such as
     * "mdm", "modem", "madam", "medium", "madame", "madman" will pass. Note how within the first four examples,
     * any number of characters can appear between each character in the original string "mdm". The last two examples
     * show that any characters can also appear after the "mdm" match.
     *
     * @param characterSequence A sequence of characters.
     * @return A {@link Predicate} that checks if the candidate string contains the {@code input} characters
     * in the same order.
     */
    public static Predicate<String> createFuzzyMatcher(final String characterSequence) {
        requireNonNull(characterSequence);

        final String anyPrintableCharacter = "\\S*";
        final String emptyString = "";
        final StringJoiner patternBuilder = new StringJoiner(anyPrintableCharacter, emptyString, anyPrintableCharacter);

        characterSequence
                .codePoints()
                .mapToObj(Character::toChars)
                .map(String::valueOf)
                .map(Pattern::quote)
                .forEach(patternBuilder::add);

        return Pattern.compile(patternBuilder.toString(), Pattern.UNICODE_CHARACTER_CLASS).asMatchPredicate();
    }
}
