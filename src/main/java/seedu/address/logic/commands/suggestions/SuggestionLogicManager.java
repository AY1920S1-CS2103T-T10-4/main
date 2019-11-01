package seedu.address.logic.commands.suggestions;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.parser.ArgumentList;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.CommandArgument;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.SuggestingCommandUtil;
import seedu.address.logic.parser.TimeBookParser;
import seedu.address.logic.parser.TimeBookParser.CommandTokens;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.ui.SuggestingCommandBox.SuggestionLogic;

/**
 * Handles the logic between the {@link seedu.address.ui.SuggestingCommandBox}, {@link Suggester} and {@link Model}.
 */
public class SuggestionLogicManager implements SuggestionLogic {
    private static final ObservableList<String> EMPTY_LIST = FXCollections.emptyObservableList();

    private final FilteredList<String> commandSuggestions;
    private final Model model;

    public SuggestionLogicManager(final Model model) {
        this(Objects.requireNonNull(model), SuggestingCommandUtil.getCommandWords());
    }

    public SuggestionLogicManager(final Model model, final List<String> commandWords) {
        this.model = Objects.requireNonNull(model);
        Objects.requireNonNull(commandWords);

        final ObservableList<String> observableListCommandWords;
        if (commandWords instanceof ObservableList) {
            observableListCommandWords = (ObservableList<String>) commandWords;
        } else {
            observableListCommandWords = FXCollections.observableList(commandWords);
        }

        this.commandSuggestions = new FilteredList<>(observableListCommandWords);
    }

    private static boolean isCaretWithinCommandWordSection(final CommandTokens commandTokens, final int caretPosition) {
        return caretPosition <= commandTokens.getCommandWordLength();
    }

    @Override
    public ObservableList<String> getSuggestions(final String commandText, final int caretPosition) {
        CollectionUtil.requireAllNonNull(commandText, caretPosition);

        final CommandTokens commandTokens;
        try {
            commandTokens = TimeBookParser.tokenizeCommand(commandText);
        } catch (ParseException e) {
            return EMPTY_LIST;
        }

        if (commandTokens.arguments.isBlank() || isCaretWithinCommandWordSection(commandTokens, caretPosition)) {
            final Predicate<String> fuzzyMatcher = SuggestingCommandUtil.createFuzzyMatcher(commandTokens.commandWord);
            commandSuggestions.setPredicate(fuzzyMatcher);
            return commandSuggestions;
        }

        final List<Prefix> prefixList = Suggester.getCommandPrefixes(commandTokens.commandWord);
        if (prefixList == null || prefixList.isEmpty()) {
            return EMPTY_LIST;
        }

        final ArgumentList argumentList = ArgumentTokenizer.orderedTokenize(commandTokens.arguments, prefixList);
        final Suggester suggester = Suggester.createSuggester(commandTokens.commandWord);

        if (suggester == null) {
            return EMPTY_LIST;
        }

        final int argRelativePosition = caretPosition - commandTokens.getCommandWordLength();
        final CommandArgument commandArgument = argumentList.getClosestCommandArgumentAtPosition(argRelativePosition);

        return suggester.getSuggestions(model, argumentList, commandArgument);
    }

    @Override
    public SelectionResult selectSuggestion(
            final String commandText, final int caretPosition, final String selectedValue) {
        CollectionUtil.requireAllNonNull(commandText, caretPosition, selectedValue);

        final CommandTokens commandTokens;
        try {
            commandTokens = TimeBookParser.tokenizeCommand(commandText);
        } catch (ParseException e) {
            return SelectionResult.of(commandText, caretPosition);
        }

        if (commandTokens.arguments.isBlank() || isCaretWithinCommandWordSection(commandTokens, caretPosition)) {
            String newCommand = selectedValue;
            final int newCaretPosition = selectedValue.length() + 1;

            if (commandTokens.arguments.isEmpty()) {
                newCommand += " ";
            } else {
                newCommand += commandTokens.arguments;
            }

            return SelectionResult.of(newCommand, newCaretPosition);
        }

        final List<Prefix> prefixList = Suggester.getCommandPrefixes(commandTokens.commandWord);
        if (prefixList == null || prefixList.isEmpty()) {
            return SelectionResult.of(selectedValue + " ");
        }

        final ArgumentList originalArgumentList = ArgumentTokenizer.orderedTokenize(
                commandTokens.arguments, prefixList);
        final ArgumentList modifiedArgumentList = (ArgumentList) originalArgumentList.clone();

        final int argumentRelativeCaret = caretPosition - commandTokens.getCommandWordLength();
        final int commandArgumentIndex = originalArgumentList.getClosestIndexAtPosition(argumentRelativeCaret);

        final CommandArgument oldCommandArgument = originalArgumentList.get(commandArgumentIndex);
        final CommandArgument newCommandArgument = oldCommandArgument.copyWithNewValue(selectedValue);
        assert !oldCommandArgument.equals(newCommandArgument) && newCommandArgument.getValue().equals(selectedValue);

        modifiedArgumentList.set(commandArgumentIndex, newCommandArgument);
        assert modifiedArgumentList.contains(newCommandArgument) && !modifiedArgumentList.contains(oldCommandArgument);

        String newCommand = commandTokens.commandWord + modifiedArgumentList.toString();
        int newCaretPosition = commandTokens.commandWord.length();
        newCaretPosition += modifiedArgumentList.calculateCaretPositionAfterIndex(commandArgumentIndex);

        return SelectionResult.of(newCommand, newCaretPosition);
    }
}
