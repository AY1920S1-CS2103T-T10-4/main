package seedu.address.ui;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import seedu.address.model.display.detailwindow.PersonSchedule;
import seedu.address.model.display.detailwindow.PersonTimeslot;
import seedu.address.model.display.schedulewindow.FreeSchedule;
import seedu.address.model.display.schedulewindow.FreeTimeslot;
import seedu.address.ui.util.DateFormatter;
import seedu.address.ui.util.TimeFormatter;

/**
 * A class to generate a schedule table (ui) from a Schedule object.
 */
public class ScheduleView extends UiPart<Region> {
    //Schedule to be received from logic MUST have timeslots in chronological order.
    //ScheduleView must be wrapped in a scroll pane otherwise the view will become distorted.

    private static final String FXML = "ScheduleView.fxml";

    protected final int oneHourLength = 60;
    protected final int preferredWidth = 140;
    protected double blockWidth = 140;
    protected final int startTime = 8;
    protected final int endTime = 20;

    @FXML
    private Label title;

    @FXML
    private ScrollPane scheduleHeaderWrapper;

    @FXML
    private ScrollPane scheduleContents;

    private GridPane scheduleHeader;
    private GridPane scheduleView;
    private List<String> colors;
    private HashMap<DayOfWeek, StackPane> dayTimeslotStackPanes = new HashMap<DayOfWeek, StackPane>();
    private int currentDay;
    private LocalDate currentDate;

    public ScheduleView(List<PersonSchedule> personSchedules, List<String> colors, String title, LocalDate date) {
        super(FXML);
        this.currentDay = LocalDateTime.now().getDayOfWeek().getValue();
        this.currentDate = date;
        this.colors = colors;
        this.title.setText(title + "'s Schedule");
        initialise();
        initialiseHeaders();
        initialiseTimeslotHeaders();
        initialiseTableCells();
        showSchedule(personSchedules);
        scheduleContents.setContent(scheduleView);
        scheduleHeaderWrapper.setContent(scheduleHeader);
    }

    /**
     * Method to initialise the FXML components of ScheduleView.
     * @return This object.
     */
    private ScheduleView initialise() {
        scheduleView = new GridPane();
        scheduleView.setStyle("-fx-border-width: 2; -fx-pref-width: 950;");
        scheduleHeader = new GridPane();
        scheduleHeader.setStyle("-fx-pref-width: 950;");
        scheduleHeaderWrapper.setMinHeight(50);
        scheduleHeaderWrapper.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scheduleHeaderWrapper.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scheduleContents.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scheduleContents.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scheduleContents.hvalueProperty().bindBidirectional(scheduleHeaderWrapper.hvalueProperty());
        return this;
    }

    /**
     * initialise top headers in the table view.
     */
    private void initialiseHeaders() {
        Region placeHolder = new Region();
        placeHolder.setId("dayLabelContainer");
        scheduleHeader.add(placeHolder, 0, 0);
        ColumnConstraints colCOffset = new ColumnConstraints();
        colCOffset.setPercentWidth(9);
        scheduleHeader.getColumnConstraints().add(colCOffset);
        //day headers
        for (int i = 1; i <= 7; i++) {
            int offset = (currentDay + i - 1) > 7 ? currentDay + i - 8 : currentDay + i - 1;
            StackPane sp = new StackPane();
            Label dayText = new Label(DayOfWeek.of(offset).toString());
            Label dayDate = new Label(DateFormatter.formatToString(currentDate.plusDays(i - 1)));
            VBox dayLabelContainer = new VBox();
            dayText.setId("dayText");
            dayDate.setId("dayDate");
            dayLabelContainer.setId("dayLabelContainer");
            dayLabelContainer.setPrefSize(preferredWidth, 50);
            ColumnConstraints colC = new ColumnConstraints();
            colC.setPercentWidth(13);
            scheduleHeader.getColumnConstraints().add(colC);
            dayLabelContainer.getChildren().addAll(dayText, dayDate);
            sp.getChildren().addAll(dayLabelContainer);
            scheduleHeader.add(sp, i, 0);
        }
    }

    /**
     * Helper method to initialise the time slot headers in the table view.
     */
    private void initialiseTimeslotHeaders() {
        //timeslot headers
        ColumnConstraints colCOffset = new ColumnConstraints();
        colCOffset.setPercentWidth(9);
        scheduleView.getColumnConstraints().add(colCOffset);
        for (int i = 1; i <= 7; i++) {
            ColumnConstraints colC = new ColumnConstraints();
            colC.setPercentWidth(13);
            scheduleView.getColumnConstraints().add(colC);
        }
        for (int j = startTime; j < endTime; j++) {
            //left-side headers
            String time = TimeFormatter.formatIntToTime(j);
            Region timeslotLeftLabelContainer = new Region();
            timeslotLeftLabelContainer.setPrefSize(preferredWidth, oneHourLength);
            timeslotLeftLabelContainer.setId("timeslotLabelContainer");
            Label timeslotLeftText = new Label(time);
            StackPane leftTimeslotHeaderContainer = new StackPane();
            leftTimeslotHeaderContainer.getChildren().addAll(timeslotLeftLabelContainer, timeslotLeftText);
            scheduleView.add(leftTimeslotHeaderContainer, 0, j - startTime);
        }
    }

    /**
     * Helper method to initialise table cells and grid lines in the table view.
     */
    private void initialiseTableCells() {
        //timeslot data
        for (int l = 1; l <= 7; l++) {
            int offsetDay = (currentDay + l - 1) > 7 ? (currentDay + l - 8) : (currentDay + l - 1);
            StackPane stackPane = new StackPane();
            VBox timeslotContainer = new VBox();
            Region firstRegionOffset = new Region();
            firstRegionOffset.setPrefSize(preferredWidth, oneHourLength / 2);
            firstRegionOffset.setId("offsetRegion");
            timeslotContainer.getChildren().add(firstRegionOffset);
            for (int k = startTime; k < endTime; k++) {
                StackPane timeslotRegion = new StackPane();
                Region timeslotMajorRegion = new Region();
                timeslotMajorRegion.setId("timeslotMajorLine");
                VBox timeslotMinorRegion = new VBox();
                Region offset = new Block(30).makeEmptyTimeslot();
                Region timeslotMinorRegion1 = new Region();
                timeslotMinorRegion1.setId("timeslotMinorLine");
                timeslotMinorRegion1.setPrefSize(preferredWidth, oneHourLength / 2.0);
                timeslotMinorRegion.getChildren().addAll(offset, timeslotMinorRegion1);
                if (k == endTime - 1) {
                    timeslotMajorRegion.setPrefSize(preferredWidth, oneHourLength / 2);
                    timeslotMajorRegion.setId("offsetRegion");
                    timeslotRegion.getChildren().addAll(timeslotMajorRegion);
                    timeslotContainer.getChildren().add(timeslotRegion);
                } else {
                    timeslotMajorRegion.setPrefSize(preferredWidth, oneHourLength);
                    timeslotRegion.getChildren().addAll(timeslotMajorRegion, timeslotMinorRegion);
                    timeslotContainer.getChildren().add(timeslotRegion);
                }
            }
            stackPane.getChildren().add(timeslotContainer);
            String altDayId = l % 2 == 0 ? "dayEvenStackPane" : "dayOddStackPane";
            stackPane.setId(altDayId);
            dayTimeslotStackPanes.put(DayOfWeek.of(offsetDay), stackPane);
            scheduleView.add(stackPane, l, 0, 1, endTime - startTime);
        }
    }

    private int getTimeDifference(int startTime, int endTime) {
        int startTimeHours = startTime / 100;
        int endTimeHours = endTime / 100;
        int hours = endTimeHours - startTimeHours;
        int startTimeMinutes = startTime % 100;
        int endTimeMinutes = endTime % 100;
        int minutes = endTimeMinutes - startTimeMinutes;
        return hours * 60 + minutes;
    }

    /**
     * Method to create a block that represents the occupied/busy time slots of an individual for a particular day.
     * @param daySchedule List that contains all occupied time slots of the individual on a particular day.
     * @param color Color that the block should be.
     * @return VBox that represents the individual's busy time slots for this particular day.
     */
    private VBox getDayVBoxOfIndividualSchedule(ArrayList<PersonTimeslot> daySchedule, String color) {
        VBox timeslotContainer = new VBox();
        timeslotContainer.setId("timeslotContainer");
        timeslotContainer.getChildren().add(new Block(30).makeEmptyTimeslot());
        int originalTimeStamp = startTime * 100;
        for (int j = 0; j < daySchedule.size(); j++) {
            PersonTimeslot timeslot = daySchedule.get(j);
            int startTime = TimeFormatter.formatTimeToInt(timeslot.getStartTime());
            int endTime = TimeFormatter.formatTimeToInt(timeslot.getEndTime());
            Region busyTimeslot = new Block(getTimeDifference(startTime, endTime)).makeColouredTimeslot(color);
            if (originalTimeStamp != startTime) {
                int timeUntilBusy = getTimeDifference(originalTimeStamp, startTime);
                Region freeTimeslot = new Block(timeUntilBusy).makeEmptyTimeslot();
                timeslotContainer.getChildren().add(freeTimeslot);
            }
            timeslotContainer.getChildren().add(busyTimeslot);
            originalTimeStamp = endTime;
        }
        return timeslotContainer;
    }

    /**
     * Method to create a block that represents free time in groups on a particular day.
     * Only called for group schedules. Free time is shown by a translucent green rectangle on the UI.
     * @param freeSchedule The common free time among groups for a particular day.
     * @return VBox that represents the free time block.
     */
    private VBox getDayVBoxOfFreeSchedule(ArrayList<FreeTimeslot> freeSchedule) {
        VBox timeslotContainer = new VBox();
        timeslotContainer.getChildren().add(new Block(30).makeEmptyTimeslot());
        int originalTimeStamp = startTime * 100;
        for (int j = 0; j < freeSchedule.size(); j++) {
            FreeTimeslot timeslot = freeSchedule.get(j);
            int startTime = TimeFormatter.formatTimeToInt(timeslot.getStartTime());
            int endTime = TimeFormatter.formatTimeToInt(timeslot.getEndTime());
            int timeslotId = timeslot.getId();
            StackPane freeTime = new Block(getTimeDifference(startTime, endTime))
                    .makeFreeTimeslot("" + timeslotId);
            if (originalTimeStamp != startTime) {
                int timeUntilNext = getTimeDifference(originalTimeStamp, startTime);
                Region untilNext = new Block(timeUntilNext).makeEmptyTimeslot();
                timeslotContainer.getChildren().add(untilNext);
            }
            timeslotContainer.getChildren().add(freeTime);
            originalTimeStamp = endTime;
        }
        return timeslotContainer;
    }

    /**
     * Method to obtain a table view of the all the schedules present in a group.
     * @param schedules An array list of schedule maps obtained from calling getScheduleMap on a Schedule Object.
     * @return  GridPane table view of schedules.
     */
    public GridPane showSchedule(List<PersonSchedule> schedules) {
        //Assign colors to each schedule.
        //Draw VBox of each individual's schedule.
        //Put VBoxes of all individuals' timeslot for the day into HBox.
        //Put HBox into dayStackPane.
        //Loop to next day.
        blockWidth = blockWidth / (schedules.size());
        for (int i = 1; i <= 7; i++) {
            StackPane dayStackPane = dayTimeslotStackPanes.get(DayOfWeek.of(i));
            HBox groupTimeslot = new HBox();
            for (int j = 0; j < schedules.size(); j++) {
                HashMap<DayOfWeek, ArrayList<PersonTimeslot>> personSchedule = schedules.get(j).getScheduleDisplay();
                VBox dayScheduleVBox = getDayVBoxOfIndividualSchedule(personSchedule.get(DayOfWeek.of(i)),
                        colors.get(j));
                HBox.setHgrow(dayScheduleVBox, Priority.ALWAYS);
                groupTimeslot.getChildren().add(dayScheduleVBox);
            }
            dayStackPane.getChildren().add(groupTimeslot);
        }
        return scheduleView;
    }

    /**
     * Method to append the free times of a group into the group schedule.
     * @param schedule A schedule that contains the available time for all group members.
     * @return The scheduleView which contains the available time slots indicated.
     */
    public GridPane setFreeTime(FreeSchedule schedule) {
        HashMap<DayOfWeek, ArrayList<FreeTimeslot>> vacantSchedule = schedule.getFreeSchedule();
        for (int i = 1; i <= 7; i++) {
            StackPane dayStackPane = dayTimeslotStackPanes.get(DayOfWeek.of(i));
            ArrayList<FreeTimeslot> freeTimes = vacantSchedule.get(DayOfWeek.of(i));
            VBox freeTimeVBox = getDayVBoxOfFreeSchedule(freeTimes);
            dayStackPane.getChildren().add(freeTimeVBox);
        }
        return scheduleView;
    }

    /**
     * Method to invoke scrolling events to the schedule view.
     */
    public void scrollNext() {
        if (scheduleContents.getVvalue() == 0) {
            scheduleContents.setVvalue(1);
        } else {
            scheduleContents.setVvalue(0);
        }
    }

    /**
     * A class to create a time slot block in the UI.
     */
    class Block {
        private int duration;
        private double heightOfTimeslot;
        public Block(int duration) {
            this.duration = duration;
            int hours = duration / 60;
            double minutes = duration % 60;
            this.heightOfTimeslot = hours * oneHourLength + (minutes / 60.0) * oneHourLength;
        }

        /**
         * Method to obtain a coloured timeslot;
         */
        private Region makeColouredTimeslot(String color) {
            Region result = new Region();
            result.setPrefSize(blockWidth, heightOfTimeslot);
            result.setStyle("-fx-background-color: " + getLinearGradient(color));
            result.setId("colouredTimeslot");
            return result;
        }

        private String getLinearGradient(String color) {
            Color lighterTone = Color.web(color).desaturate();
            String lighterToneHex = "#" + Integer.toHexString(lighterTone.hashCode());
            String linearGradient = "linear-gradient(" + lighterToneHex + " 0%, " + color + " 20%, "
                    + color + " 80%, " + lighterToneHex + " 100%);";
            return linearGradient;
        }

        /**
         * Method to create a transparent block in the table view to indicate free time.
         * @return Region that represents the free time in the schedule.
         */
        private Region makeEmptyTimeslot() {
            Region result = new Region();
            result.setPrefSize(blockWidth, heightOfTimeslot);
            return result;
        }

        /**
         * Method to create a free time slot block with an id.
         * @param text The id to be set in the block.
         * @return StackPane.
         */
        private StackPane makeFreeTimeslot(String text) {
            StackPane freeTimeslot = new StackPane();
            Label label = new Label(text);
            label.setPrefSize(blockWidth, heightOfTimeslot);
            Region region = makeColouredTimeslot("lightgreen");
            region.setId("freeTimeslotBlock");
            freeTimeslot.setId("freeTimeslot");
            freeTimeslot.getChildren().addAll(label, region);
            //Need to handle cases when timeslot height is less than 15px otherwise
            //the result block size will be inaccurate.
            if (heightOfTimeslot < 15) {
                label.setStyle("-fx-font-size: " + (heightOfTimeslot - 5) + ";");
            }
            return freeTimeslot;
        }
    }
}
