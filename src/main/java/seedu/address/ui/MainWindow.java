package seedu.address.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.export.VisualExporter;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.display.detailwindow.DetailWindowDisplay;
import seedu.address.model.display.detailwindow.DetailWindowDisplayType;
import seedu.address.model.display.sidepanel.SidePanelDisplayType;
import seedu.address.ui.SuggestingCommandBox.SuggestionLogic;
import seedu.address.ui.util.ColorGenerator;
import seedu.address.ui.util.DefaultStartView;
import seedu.address.ui.util.GroupDetailsExport;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private GroupListPanel groupListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private ScheduleView scheduleView;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane sideBarPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane detailsViewPlaceholder;

    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        personListPanel = new PersonListPanel(logic.getFilteredPersonDisplayList());
        TabPanel tabPanel = new TabPanel();
        //To do for logic -> getGroupList.
        groupListPanel = new GroupListPanel(logic.getFilteredGroupDisplayList());
        tabPanel.setContent(personListPanel.getRoot(), groupListPanel.getRoot());
        sideBarPlaceholder.getChildren().add(tabPanel.getTabs());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox;
        if (logic instanceof SuggestionLogic) {
            logger.info("logic supports suggestions, loading SuggestingCommandBox");
            final SuggestionLogic suggestionLogic = (SuggestionLogic) logic;
            commandBox = new SuggestingCommandBox(this::executeCommand, suggestionLogic);
        } else {
            logger.warning("logic does not suggestions, loading CommandBox");
            commandBox = new CommandBox(this::executeCommand);
        }

        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        //setting up default detailsview
        detailsViewPlaceholder.getChildren().add(new DefaultStartView(400, 400).getRoot());
    }

    /**
     * Handles change of details view
     * @param details details to be set inside detailsViewPlaceHolder in MainWindow.
     */
    public void handleChangeOnDetailsView(Node details) {
        detailsViewPlaceholder.getChildren().clear();
        detailsViewPlaceholder.getChildren().add(details);
    }

    /**
     * Handles change of sidepanel view.
     */
    public void handleChangeToTabsPanel() {
        sideBarPlaceholder.getChildren().clear();
        personListPanel = new PersonListPanel(logic.getFilteredPersonDisplayList());
        TabPanel tabPanel = new TabPanel();
        groupListPanel = new GroupListPanel(logic.getFilteredGroupDisplayList());
        tabPanel.setContent(personListPanel.getRoot(), groupListPanel.getRoot());
        sideBarPlaceholder.getChildren().add(tabPanel.getTabs());
    }

    /**
     * Handles tab switch view.
     * @param type Tab number to be switched to. 1 for person, 2 for group.
     */
    public void handleTabSwitch(SidePanelDisplayType type) {
        if (type.equals(SidePanelDisplayType.PERSONS)) {
            TabPane tabPane = (TabPane) sideBarPlaceholder.getChildren().get(0);
            tabPane.getSelectionModel().select(0);
        } else if (type.equals(SidePanelDisplayType.GROUPS)) {
            TabPane tabPane = (TabPane) sideBarPlaceholder.getChildren().get(0);
            tabPane.getSelectionModel().select(1);
        } else {
            //Tabpane remain the same.
        }
    }

    /**
     * Handles switching tabs to view person's or group's detail.
     */
    public void handleSidePanelChange(Node details) {
        sideBarPlaceholder.getChildren().clear();
        sideBarPlaceholder.getChildren().add(details);
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Method to handle scrolling events.
     */
    private void handleScroll() {
        if (scheduleView == null) {
            //No schedule has been loaded yet. Do nothing.
        } else {
            scheduleView.scrollNext();
        }
    }

    /**
     * Method to handle exportation of view.
     * @param detailWindowDisplay Details to be exported.
     */
    private void handleExport(DetailWindowDisplay detailWindowDisplay) {
        DetailWindowDisplayType type = detailWindowDisplay.getDetailWindowDisplayType();
        if (type.equals(DetailWindowDisplayType.PERSON)) {
            PersonDetailsView personDetailsView = new PersonDetailsView(detailWindowDisplay);
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(personDetailsView.getRoot());
            Scene scene = new Scene(stackPane);
            try {
                VisualExporter.exportTo(stackPane, "png", "./export.png");
            } catch (IOException e) {
                resultDisplay.setFeedbackToUser("Error exporting");
            }
        } else {
            GroupDetailsExport groupDetailsView = new GroupDetailsExport(detailWindowDisplay);
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(groupDetailsView.getRoot());
            Scene scene = new Scene(stackPane);
            try {
                VisualExporter.exportTo(stackPane, "png", "./export.png");
            } catch (IOException e) {
                resultDisplay.setFeedbackToUser("Error exporting");
            }
        }
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
            DetailWindowDisplay detailWindowDisplay = logic.getMainWindowDisplay();
            DetailWindowDisplayType displayType = detailWindowDisplay.getDetailWindowDisplayType();
            ArrayList<String> colors = ColorGenerator
                    .generateColorList(detailWindowDisplay.getWeekSchedules().size());
            switch(displayType) {
            case PERSON:
                //There is only 1 schedule in the detailWindowDisplay
                ScheduleView personScheduleView = new ScheduleView(detailWindowDisplay.getWeekSchedules().get(0));
                handleChangeOnDetailsView(personScheduleView.getRoot());
                handleSidePanelChange(
                        new PersonDetailCard(detailWindowDisplay
                                .getWeekSchedules()
                                .get(0)
                                .getPersonDisplay())
                                .getRoot());
                scheduleView = personScheduleView;
                break;
            case GROUP:
                ScheduleView groupScheduleView = new ScheduleView(detailWindowDisplay.getWeekSchedules(),
                        colors,
                        detailWindowDisplay.getGroupDisplay().getGroupName(), detailWindowDisplay.getFreeSchedule());
                handleChangeOnDetailsView(groupScheduleView.getRoot());
                handleSidePanelChange(new GroupInformation(detailWindowDisplay, colors).getRoot());
                scheduleView = groupScheduleView;
                break;
            case DEFAULT:
                handleChangeToTabsPanel();
                handleTabSwitch(logic.getSidePanelDisplay().getSidePanelDisplayType());
                break;
            default:
                //Nothing to show
                break;
            }

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            if (commandResult.isExport()) {
                handleExport(detailWindowDisplay);
            }

            if (commandResult.isScroll()) {
                handleScroll();
            }

            if (commandResult.isPopUp()) {
                //WIP
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

}
