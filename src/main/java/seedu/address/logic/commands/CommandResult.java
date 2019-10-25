package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /**
     * Help information should be shown to the user.
     */
    private final boolean showHelp;

    /**
     * The application should exit.
     */
    private final boolean exit;

    /**
     * The application should export a file.
     */
    private final boolean export;

    /**
     * The application should scroll down.
     */
    private final boolean scroll;

    /**
     * The application should have a popup.
     */
    private final boolean popUp;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.export = false;
        this.scroll = false;
        this.popUp = false;
    }

    /**
     * Constructs an alternative CommandResult that would affect the UI.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, boolean export) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.export = export;
        this.scroll = false;
        this.popUp = false;
    }

    /**
     * Constructs an alternative CommandResult that would affect the UI.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, boolean export, boolean scroll) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.export = export;
        this.scroll = scroll;
        this.popUp = false;
    }

    /**
     * Constructs an alternative CommandResult that would affect the UI.
     */
    public CommandResult(String feedbackToUser,
                         boolean showHelp, boolean exit, boolean export, boolean scroll, boolean popUp) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.export = export;
        this.scroll = scroll;
        this.popUp = popUp;
    }


    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean isExport() {
        return export;
    }

    public boolean isScroll() {
        return scroll;
    }

    public boolean isPopUp() {
        return popUp;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && export == otherCommandResult.export;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, export);
    }

}
