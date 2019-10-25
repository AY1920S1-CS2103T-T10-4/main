package seedu.address.ui.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seedu.address.model.display.detailwindow.DayTimeslot;
import seedu.address.model.display.detailwindow.WeekSchedule;

/**
 * A class to generate a schedule table (ui) from a Schedule object.
 */
public class ScheduleExport {
    //Schedule to be received from logic MUST have timeslots in chronological order.
    //ScheduleExport must be wrapped in a scroll pane otherwise the view will become distorted.
    private static final String FXML = "ScheduleExport.fxml";
    private static ArrayList<String> dayNames = new ArrayList<String>(List.of("Monday", "Tuesday",
            "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
    private static ArrayList<String> listOfColors = new ArrayList<String>(List.of("darkred", "navy", "darkgreen",
            "darkorange", "lightslategray", "orchid", "teal", "darkmagenta"));

    private GridPane scheduleView;
    private HashMap<DayOfWeek, StackPane> dayTimeslotStackPanes = new HashMap<DayOfWeek, StackPane>();
    private List<String> colors;
    private int oneHourLength = 60;
    private int preferredWidth = 100;
    private double blockWidth = 100;
    private int startTime = 8;
    private int endTime = 20;
    private int currentDay;
    private LocalDate currentDate;

    public ScheduleExport() {
        this.currentDay = LocalDateTime.now().getDayOfWeek().getValue();
        this.currentDate = LocalDate.now();
        initialise();
        initialiseHeaders();
        initialiseTableCells();
    }

    public ScheduleExport(WeekSchedule weekSchedule) {
        this.currentDay = LocalDateTime.now().getDayOfWeek().getValue();
        this.currentDate = LocalDate.now();
        initialise();
        initialiseHeaders();
        initialiseTableCells();
        HashMap<DayOfWeek, ArrayList<DayTimeslot>> scheduleMap = weekSchedule.getWeekSchedule();
        showIndividualSchedule(scheduleMap, listOfColors.get((int) (Math.random() * (listOfColors.size() - 1))));
    }

    public ScheduleExport(ArrayList<WeekSchedule> weekSchedules, List<String> colors) {
        this.currentDay = LocalDateTime.now().getDayOfWeek().getValue();
        this.currentDate = LocalDate.now();
        this.colors = colors;
        initialise();
        initialiseHeaders();
        initialiseTableCells();
        showGroupSchedule(weekSchedules);
    }

    private ScheduleExport initialise() {
        scheduleView = new GridPane();
        scheduleView.setStyle("-fx-border-width: 2; -fx-border-color: transparent; -fx-pref-width: 1100;");
        return this;
    }

    /**
     * Helper method to initialise the headers in the table view.
     */
    private void initialiseHeaders() {
        //initialise headers
        Region placeHolder = new Region();
        placeHolder.setStyle("-fx-background-color: transparent");
        scheduleView.add(placeHolder, 0, 0);
        Region secondPlaceHolder = new Region();
        secondPlaceHolder.setStyle("-fx-background-color: transparent");
        scheduleView.add(secondPlaceHolder, 8, 0);
        ColumnConstraints colCOffset = new ColumnConstraints();
        colCOffset.setPercentWidth(5);
        scheduleView.getColumnConstraints().add(colCOffset);
        //day headers
        for (int i = 1; i <= 7; i++) {
            int offset = (currentDay + i - 1) > 7 ? currentDay + i - 8 : currentDay + i - 1;
            StackPane sp = new StackPane();
            Label dayText = new Label(DayOfWeek.of(offset).toString());
            Label dayDate = new Label(DateFormatter.formatToString(currentDate.plusDays(i - 1)));
            VBox dayLabelContainer = new VBox();
            dayLabelContainer.setPrefSize(preferredWidth, 50);
            ColumnConstraints colC = new ColumnConstraints();
            colC.setPercentWidth(12.857);
            scheduleView.getColumnConstraints().add(colC);
            dayLabelContainer.setStyle("-fx-background-color: white; -fx-border-width: 2; -fx-alignment: center;");
            dayLabelContainer.getChildren().addAll(dayText, dayDate);
            sp.getChildren().addAll(dayLabelContainer);
            scheduleView.add(sp, i, 0);
        }
        ColumnConstraints colCOffset2 = new ColumnConstraints();
        colCOffset2.setPercentWidth(5);
        scheduleView.getColumnConstraints().add(colCOffset2);
        //timeslot headers
        for (int j = startTime; j < endTime; j++) {
            //left-side headers
            String time = TimeFormatter.formatIntToTime(j);
            Region timeslotLeftLabelContainer = new Region();
            timeslotLeftLabelContainer.setPrefSize(preferredWidth, oneHourLength);
            timeslotLeftLabelContainer.setStyle("-fx-background-color: white; -fx-border-color: white;");
            Label timeslotLeftText = new Label(time);
            StackPane leftTimeslotHeaderContainer = new StackPane();
            leftTimeslotHeaderContainer.getChildren().addAll(timeslotLeftLabelContainer, timeslotLeftText);
            scheduleView.add(leftTimeslotHeaderContainer, 0, j - startTime + 1);

            //right-side headers
            Region timeslotRightLabelContainer = new Region();
            timeslotRightLabelContainer.setPrefSize(preferredWidth, oneHourLength);
            timeslotRightLabelContainer.setStyle("-fx-background-color: white; -fx-border-color: white;");
            Label timeslotRightText = new Label(time);
            StackPane rightTimeslotHeaderContainer = new StackPane();
            rightTimeslotHeaderContainer.getChildren().addAll(timeslotRightLabelContainer, timeslotRightText);
            scheduleView.add(rightTimeslotHeaderContainer, 8, j - startTime + 1);
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
            firstRegionOffset.setStyle("-fx-border-color: lightgrey; -fx-border-style: solid none none solid");
            timeslotContainer.getChildren().add(firstRegionOffset);
            for (int k = startTime; k < endTime; k++) {
                StackPane timeslotRegion = new StackPane();
                Region timeslotMajorRegion = new Region();
                timeslotMajorRegion.setStyle("-fx-border-color: lightgrey; -fx-border-style: solid none none solid;");
                VBox timeslotMinorRegion = new VBox();
                Region offset = makeEmptyTimeslot(30);
                Region timeslotMinorRegion1 = new Region();
                timeslotMinorRegion1.setStyle("-fx-border-color: lightgrey; -fx-border-style: dotted none none dotted");
                timeslotMinorRegion1.setPrefSize(preferredWidth, oneHourLength / 2.0);
                timeslotMinorRegion.getChildren().addAll(offset, timeslotMinorRegion1);
                if (k == endTime - 1) {
                    timeslotMajorRegion.setPrefSize(preferredWidth, oneHourLength / 2);
                    timeslotRegion.getChildren().addAll(timeslotMajorRegion);
                    timeslotContainer.getChildren().add(timeslotRegion);
                } else {
                    timeslotMajorRegion.setPrefSize(preferredWidth, oneHourLength);
                    timeslotRegion.getChildren().addAll(timeslotMajorRegion, timeslotMinorRegion);
                    timeslotContainer.getChildren().add(timeslotRegion);
                }
            }
            stackPane.getChildren().add(timeslotContainer);
            dayTimeslotStackPanes.put(DayOfWeek.of(offsetDay), stackPane);
            scheduleView.add(stackPane, l, 1, 1, endTime - startTime);
        }
    }

    /**
     * Method to create a transparent block in the table view to indicate free time.
     * @param durationMinutes Length of the block in minutes.
     * @return Region that represents the free time in the schedule.
     */
    private Region makeEmptyTimeslot(int durationMinutes) {
        Region result = new Region();
        int hours = durationMinutes / 60;
        int minutes = durationMinutes % 60;
        double heightOfTimeslot = hours * oneHourLength + (minutes / 60.0) * oneHourLength;
        result.setPrefSize(blockWidth, heightOfTimeslot);
        return result;
    }

    /**
     * Helper method to create a block of coloured timeslot in the table view.
     * @param durationMinutes Length of the block in minutes.
     * @param color Color of the block.
     * @return Region to be placed in the table view.
     */
    private Region makeColouredTimeslot(int durationMinutes, String color) {
        Region result = new Region();
        int hours = durationMinutes / 60;
        int minutes = durationMinutes % 60;
        double heightOfTimeslot = hours * oneHourLength + (minutes / 60.0) * oneHourLength;
        result.setPrefSize(blockWidth, heightOfTimeslot);
        //result.setMaxWidth(blockWidth); setsMaxWidth of busy timeslot.
        result.setStyle("-fx-background-color: " + color + "; -fx-border-width: 2; -fx-background-radius: 5;");
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        result.setEffect(shadow);
        return result;
    }

    private int getTimeDifference(int startTime, int endTime) {
        int hours = (endTime - startTime) / 100;
        int minutes = (endTime - startTime) % 100;
        return hours * 60 + minutes;
    }

    private VBox getDayVBoxOfIndividualSchedule(ArrayList<DayTimeslot> daySchedule, String color) {
        VBox timeslotContainer = new VBox();
        timeslotContainer.setStyle("-fx-padding: 0 2 0 2; -fx-border-width: 2;");
        timeslotContainer.getChildren().add(makeEmptyTimeslot(30));
        int originalTimeStamp = startTime * 100;
        for (int j = 0; j < daySchedule.size(); j++) {
            DayTimeslot timeslot = daySchedule.get(j);
            int startTime = TimeFormatter.formatTimeToInt(timeslot.getStartTime());
            int endTime = TimeFormatter.formatTimeToInt(timeslot.getEndTime());
            Region busyTimeslot = makeColouredTimeslot(getTimeDifference(startTime, endTime), color);
            if (originalTimeStamp != startTime) {
                int timeUntilBusy = getTimeDifference(originalTimeStamp, startTime);
                Region freeTimeslot = makeEmptyTimeslot(timeUntilBusy);
                timeslotContainer.getChildren().add(freeTimeslot);
            }
            timeslotContainer.getChildren().add(busyTimeslot);
            originalTimeStamp = endTime;
        }
        return timeslotContainer;
    }

    /**
     * Method to obtain a table view of an individual's schedule.
     * @param scheduleMap ScheduleMap obtained by calling getScheduleMap on a Schedule object.
     * @param color Color of the blocks in the table view.
     * @return  GridPane table view of the individual's schedule.
     */
    public GridPane showIndividualSchedule(HashMap<DayOfWeek, ArrayList<DayTimeslot>> scheduleMap,
                                           String color) {
        for (int i = 1; i <= 7; i++) {
            ArrayList<DayTimeslot> daySchedule = scheduleMap.get(DayOfWeek.of(i));
            StackPane dayStackPane = dayTimeslotStackPanes.get(DayOfWeek.of(i));
            VBox timeslotContainer = getDayVBoxOfIndividualSchedule(daySchedule, color);
            dayStackPane.getChildren().add(timeslotContainer);
        }
        return scheduleView;
    }

    /**
     * Method to obtain a table view of the all the schedules present in a group.
     * @param schedules An array list of schedule maps obtained from calling getScheduleMap on a Schedule Object.
     * @return  GridPane table view of schedules.
     */
    public GridPane showGroupSchedule(ArrayList<WeekSchedule> schedules) {
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
                HashMap<DayOfWeek, ArrayList<DayTimeslot>> personSchedule = schedules.get(j).getWeekSchedule();
                VBox dayScheduleVBox = getDayVBoxOfIndividualSchedule(personSchedule.get(DayOfWeek.of(i)),
                        colors.get(j));
                HBox.setHgrow(dayScheduleVBox, Priority.ALWAYS);
                groupTimeslot.getChildren().add(dayScheduleVBox);
            }
            dayStackPane.getChildren().add(groupTimeslot);
        }
        return scheduleView;
    }

    public GridPane getScheduleView() {
        return scheduleView;
    }

}
