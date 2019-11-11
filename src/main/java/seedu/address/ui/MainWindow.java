package seedu.address.ui;

import java.io.IOException;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
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
import seedu.address.logic.export.Exporter;
import seedu.address.logic.export.GroupScheduleExporter;
import seedu.address.logic.export.IndividualScheduleExporter;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.display.scheduledisplay.GroupScheduleDisplay;
import seedu.address.model.display.scheduledisplay.HomeScheduleDisplay;
import seedu.address.model.display.scheduledisplay.PersonScheduleDisplay;
import seedu.address.model.display.scheduledisplay.ScheduleDisplay;
import seedu.address.model.display.scheduledisplay.ScheduleState;
import seedu.address.model.display.sidepanel.SidePanelDisplayType;
import seedu.address.model.display.timeslots.PersonTimeslot;
import seedu.address.ui.SuggestingCommandBox.SuggestionLogic;
import seedu.address.ui.home.DefaultStartView;
import seedu.address.ui.popup.LocationPopup;
import seedu.address.ui.popup.LocationsView;
import seedu.address.ui.popup.TimeslotPopup;
import seedu.address.ui.popup.TimeslotView;
import seedu.address.ui.schedule.GroupInformation;
import seedu.address.ui.schedule.PersonDetailCard;
import seedu.address.ui.schedule.ScheduleViewManager;
import seedu.address.ui.schedule.exceptions.InvalidScheduleViewException;
import seedu.address.ui.util.ColorGenerator;

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
    private TabPanel tabPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private ScheduleViewManager scheduleViewManager;
    private CommandBox commandBox;

    private SidePanelDisplayType currentSidePanelDisplay;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane sideBarPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    //@FXML
    //private StackPane statusbarPlaceholder;

    @FXML
    private StackPane scheduleDisplayPlaceholder;

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
     *
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
        tabPanel = new TabPanel();
        //To do for logic -> getGroupList.
        groupListPanel = new GroupListPanel(logic.getFilteredGroupDisplayList());
        tabPanel.setContent(personListPanel.getRoot(), groupListPanel.getRoot());
        sideBarPlaceholder.getChildren().add(tabPanel.getTabs());
        currentSidePanelDisplay = SidePanelDisplayType.TABS;

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        //StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        //statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

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
        scheduleDisplayPlaceholder.getChildren().add(new DefaultStartView(logic.getScheduleDisplay()
                .getPersonSchedules().get(0))
                .getRoot());
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
     * Sets graphic of the schedule view window.
     * @param graphic details to be set inside detailsViewPlaceHolder in MainWindow.
     */
    public void setGraphicForScheduleDisplay(Node graphic) {
        scheduleDisplayPlaceholder.getChildren().clear();
        scheduleDisplayPlaceholder.getChildren().add(graphic);
    }

    /**
     * Handles change of sidepanel view.
     */
    public void handleChangeToTabsPanel() {
        sideBarPlaceholder.getChildren().clear();
        personListPanel = new PersonListPanel(logic.getFilteredPersonDisplayList());
        groupListPanel = new GroupListPanel(logic.getFilteredGroupDisplayList());
        tabPanel.setContent(personListPanel.getRoot(), groupListPanel.getRoot());
        sideBarPlaceholder.getChildren().add(tabPanel.getTabs());
        currentSidePanelDisplay = SidePanelDisplayType.TABS;
    }

    /**
     * Handles tab switch view.
     */
    public void handleTabSwitch() {
        if (!currentSidePanelDisplay.equals(SidePanelDisplayType.TABS)) {
            //Do nothing.
        } else if (tabPanel.getTabs().getSelectionModel().getSelectedIndex() == 0) {
            tabPanel.getTabs().getSelectionModel().select(1);
        } else {
            tabPanel.getTabs().getSelectionModel().select(0);
        }
    }

    /**
     * Sets the graphic for the side panel.
     * @param graphic The graphic to be placed in the side panel.
     * @param type The type.
     */
    public void setSidePanelGraphic(Node graphic, SidePanelDisplayType type) {
        sideBarPlaceholder.getChildren().clear();
        sideBarPlaceholder.getChildren().add(graphic);
        currentSidePanelDisplay = type;
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
        if (scheduleViewManager == null) {
            //No schedule has been loaded yet. Do nothing.
        } else {
            scheduleViewManager.scrollNext();
        }
    }

    /**
     * Method to handle exportation of the current schedule view.
     */
    private void handleExport() {
        ScheduleState type = scheduleViewManager.getScheduleWindowDisplayType();
        if (type.equals(ScheduleState.PERSON)) {
            Exporter exporter = new IndividualScheduleExporter(scheduleViewManager.getScheduleViewCopy(),
                    "png", "./export.png");
            try {
                exporter.export();
            } catch (IOException e) {
                resultDisplay.setFeedbackToUser("Error exporting");
            }
        } else if (type.equals(ScheduleState.GROUP)) {
            GroupScheduleDisplay groupScheduleDisplay = (GroupScheduleDisplay) logic.getScheduleDisplay();
            GroupInformation groupInformation = new GroupInformation(groupScheduleDisplay.getPersonDisplays(),
                    null, groupScheduleDisplay.getGroupDisplay(),
                    ColorGenerator::generateColor);
            Exporter exporter = new GroupScheduleExporter(scheduleViewManager.getScheduleViewCopy(), groupInformation,
                    "png", "./export.png");
            try {
                exporter.export();
            } catch (IOException e) {
                resultDisplay.setFeedbackToUser("Error exporting");
            }
        }
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

            ScheduleDisplay scheduleDisplay = logic.getScheduleDisplay();
            //Command results that require early return statements.
            if (commandResult.isScroll()) {
                handleScroll();
                return commandResult;
            }

            if (commandResult.isSwitchTabs()) {
                handleTabSwitch();
                return commandResult;
            }

            if (commandResult.isToggleNextWeek()) {
                scheduleViewManager.toggleNext();
                setGraphicForScheduleDisplay(scheduleViewManager.getScheduleView().getRoot());
                return commandResult;
            }

            if (commandResult.isExport()) {
                handleExport();
                return commandResult;
            }

            if (commandResult.isFilter()) {
                GroupScheduleDisplay groupScheduleDisplay = (GroupScheduleDisplay) scheduleDisplay;
                if (!groupScheduleDisplay.getFilteredNames().isEmpty()) {
                    setSidePanelGraphic(new GroupInformation(groupScheduleDisplay.getPersonDisplays(),
                            groupScheduleDisplay.getFilteredNames().get(), groupScheduleDisplay.getGroupDisplay(),
                            ColorGenerator::generateColor).getRoot(), SidePanelDisplayType.GROUP);
                    scheduleViewManager.filterPersonsFromSchedule(groupScheduleDisplay.getFilteredNames().get());
                    setGraphicForScheduleDisplay(scheduleViewManager.getScheduleView().getRoot());
                }
                return commandResult;
            }

            if (commandResult.isSelect()) {
                PersonTimeslot personTimeslot;
                if (commandResult.getPersonTimeslotData().isPresent()) {
                    personTimeslot = commandResult.getPersonTimeslotData().get();

                    TimeslotView timeslotView = new TimeslotView(personTimeslot);
                    new TimeslotPopup(timeslotView.getRoot()).show();

                }
                return commandResult;
            }

            if (commandResult.isPopUp()) {
                LocationsView locationsView = new LocationsView(commandResult.getLocationData());
                new LocationPopup(locationsView.getRoot()).show();
                return commandResult;
            }

            ScheduleState displayType = scheduleDisplay.getState();

            if (ScheduleViewManager.getInstanceOf(scheduleDisplay) != null) {
                scheduleViewManager = ScheduleViewManager.getInstanceOf(scheduleDisplay);
            }


            switch (displayType) {
            case PERSON:
                PersonScheduleDisplay personScheduleDisplay = (PersonScheduleDisplay) scheduleDisplay;
                //There is only 1 schedule in the scheduleWindowDisplay
                setGraphicForScheduleDisplay(scheduleViewManager.getScheduleView().getRoot());
                setSidePanelGraphic(
                        new PersonDetailCard(personScheduleDisplay
                                .getPersonSchedules()
                                .get(0)
                                .getPersonDisplay())
                                .getRoot(), SidePanelDisplayType.PERSON);
                break;
            case GROUP:
                GroupScheduleDisplay groupScheduleDisplay = (GroupScheduleDisplay) scheduleDisplay;
                setGraphicForScheduleDisplay(scheduleViewManager.getScheduleView().getRoot());
                setSidePanelGraphic(new GroupInformation(groupScheduleDisplay.getPersonDisplays(), null,
                                groupScheduleDisplay.getGroupDisplay(), ColorGenerator::generateColor).getRoot(),
                        SidePanelDisplayType.GROUP);
                break;
            case HOME:
                HomeScheduleDisplay homeScheduleDisplay = (HomeScheduleDisplay) scheduleDisplay;

                setGraphicForScheduleDisplay(new DefaultStartView(homeScheduleDisplay
                        .getPersonSchedules().get(0))
                        .getRoot());
                handleChangeToTabsPanel();
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
            return commandResult;
        } catch (InvalidScheduleViewException e) {
            logger.severe("Schedule(s) given is/are not valid. Database must have been corrupted.");
            resultDisplay.setFeedbackToUser("Database corrupted. " + e.getMessage());
            return new CommandResult("Database corrupted");
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            commandBox.commandTextField.clear();
            throw e;
        }
    }
}
