package seedu.address.ui;

import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanExpression;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.stage.Popup;
import javafx.stage.Window;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.CommandHistory;
import seedu.address.logic.parser.SuggestingCommandUtil;

/**
 * The UI component that is responsible for receiving user command inputs and offering user command suggestions.
 */
public class SuggestingCommandBox extends CommandBox {
    private final Popup popup = new Popup();
    private final ListView<String> listView = new ListView<>();
    private final ObservableList<String> commandSuggestions;
    private final FilteredList<String> filteredCommandSuggestions;
    private final CommandHistory commandHistory = new CommandHistory();
    private SuggestionMode suggestionMode = SuggestionMode.COMMAND_SUGGESTION;

    public SuggestingCommandBox(CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.commandSuggestions = SuggestingCommandUtil.getCommandWords();
        filteredCommandSuggestions = new FilteredList<>(this.commandSuggestions);

        setupListView();
        setupPopup();
        setupHistoryNavigation();
    }

    private void setupPopup() {
        popup.setAutoFix(false);
        popup.getContent().setAll(listView);
        bindPopupPosition();
        bindShowHidePopup();
    }

    /**
     * Setup the necessary bindings that will cause the popup to show or hide.
     * Popup will only be shown when the command TextField is in focus and the user has typed something.
     */
    private void bindShowHidePopup() {
        final BooleanExpression isCommandTextFieldFocused = commandTextField.focusedProperty();
        final BooleanExpression hasInput = commandTextField.textProperty().isNotEmpty();

        final BooleanExpression shouldShowPopupExpression = isCommandTextFieldFocused.and(hasInput);
        final Consumer<Window> setupShowHide = window -> {
            shouldShowPopupExpression.addListener((unused1, unused2, shouldShowPopup) -> {
                if (shouldShowPopup) {
                    popup.show(window);
                } else {
                    popup.hide();
                }
            });
        };
        UiUtil.onWindowReady(commandTextField, setupShowHide);
    }

    /**
     * Setup the necessary bindings that will reposition the suggestions Popup when the command TextField moves.
     */
    private void bindPopupPosition() {
        final InvalidationListener repositionPopup = (observable -> {
            if (!popup.isShowing()) {
                return;
            }

            final double commandTextFieldHeight = commandTextField.getHeight();
            final double popupHeight = popup.getHeight();
            final double fullHeight = commandTextFieldHeight + popupHeight;

            double verticalOffset;

            // calculate the expected bottom-left Point2D of the popup window if it's placed below the command input box
            final Point2D popupBottomLeftPoint = commandTextField.localToScreen(0, fullHeight);
            if (UiUtil.isPointUserVisible(popupBottomLeftPoint, UiUtil.Bounds.VERTICAL)) {
                // there's enough space to place the popup window below the command input box, so we'll do that
                verticalOffset = commandTextFieldHeight;
            } else {
                // not enough space to place the popup window below the command input box, so we'll place it above
                // instead
                verticalOffset = popupHeight * -1;
            }

            final Point2D absolutePosition = commandTextField.localToScreen(0, verticalOffset);
            popup.setX(absolutePosition.getX());
            popup.setY(absolutePosition.getY());
        });

        final Consumer<Window> setupBindings = window -> {
            window.xProperty().addListener(repositionPopup);
            window.yProperty().addListener(repositionPopup);
            window.heightProperty().addListener(repositionPopup);
            popup.showingProperty().addListener((unused1, unused2, isShowing) -> {
                // TODO: find a better way to force a popup reposition when it toggles from hidden to shown state
                if (!isShowing) {
                    return;
                }
                Platform.runLater(() -> {
                    repositionPopup.invalidated(null);
                });
            });
        };

        UiUtil.onWindowReady(commandTextField, setupBindings);
    }

    private void setupListView() {
        listView.setId("suggestions-list");
        listView.setMaxHeight(100); // TODO: flexible height
        listView.setFocusTraversable(false);
        listView.setItems(filteredCommandSuggestions);
        listView.prefWidthProperty().bind(commandTextField.widthProperty());
        UiUtil.redirectKeyCodeEvents(commandTextField, listView, KeyCode.TAB);

        final var listSelection = listView.getSelectionModel();

        popup.showingProperty().addListener((unused1, unused2, isShowing) -> {
            // pre-select the first item in the list when suggestions are being shown
            if (!isShowing || !listSelection.isEmpty()) {
                return;
            }

            listSelection.selectFirst();
        });

        UiUtil.addKeyCodeListener(listView, KeyCode.TAB, keyEvent -> {
            if (listSelection.isEmpty()) {
                if (listView.getItems().isEmpty()) {
                    return;
                }
                listSelection.selectFirst();
            }
            keyEvent.consume();

            final String selectedCommand = listSelection.getSelectedItem();
            commandTextField.setText(selectedCommand + " ");
            commandTextField.positionCaret(Integer.MAX_VALUE);
        });

        commandTextField.textProperty().addListener((unused1, unused2, userCommand) -> {
            final String userCommandWord = StringUtil.substringBefore(userCommand, " ");

            if (commandSuggestions.contains(userCommandWord)) {
                // the userCommandWord exactly matches a command, so we stop showing suggestions
                filteredCommandSuggestions.setPredicate((commandWord) -> false);
            } else {
                final Predicate<String> fuzzyMatcher = SuggestingCommandUtil.createFuzzyMatcher(userCommandWord);
                filteredCommandSuggestions.setPredicate(fuzzyMatcher);
            }
        });
    }

    private void setupHistoryNavigation() {
        final EnumSet<KeyCode> commandHistoryNavigationKeys = EnumSet.of(KeyCode.UP, KeyCode.DOWN);

        // Redirect the UP/DOWN keypresses to the listView only when there is a command in the commandTextField
        UiUtil.redirectKeyCodeEvents(commandTextField, listView, commandHistoryNavigationKeys, (keyEvent) -> {
            if (commandTextField.getText().isEmpty()) {
                suggestionMode = SuggestionMode.HISTORY_COMMAND_NAVIGATION;
                return false;
            }
            return true;
        });

        /*
         When an UP/DOWN keypress is triggered on the commandTextField and is not redirected,
         go through the user's past commands
        */
        UiUtil.addKeyCodeListener(commandTextField, commandHistoryNavigationKeys, keyEvent -> {
            if (commandTextField.getText().isEmpty()) {
                commandHistory.resetHistoryPointer();
            } else if (suggestionMode == SuggestionMode.COMMAND_SUGGESTION) {
                return;
            }

            final KeyCode userDirection = keyEvent.getCode();
            String commandText = null;

            switch (userDirection) {
            case UP:
                commandText = commandHistory.getPreviousCommand();
                break;
            case DOWN:
                commandText = commandHistory.getNextCommand();
                break;
            default:
                throw new IllegalStateException("Unexpected KeyCode: " + userDirection);
            }

            if (null != commandText) {
                commandTextField.setText(commandText);
                commandTextField.positionCaret(commandText.length());
            }
        });
    }

    @FXML
    @Override
    protected void handleCommandEntered() {
        final String userInput = commandTextField.getText();
        commandHistory.add(0, userInput);
        suggestionMode = SuggestionMode.COMMAND_SUGGESTION;
        commandHistory.resetHistoryPointer();

        super.handleCommandEntered();
    }

    /**
     * Suggestion Modes of the SuggestingCommandBox class
     */
    enum SuggestionMode {
        COMMAND_SUGGESTION, HISTORY_COMMAND_NAVIGATION
    }
}
