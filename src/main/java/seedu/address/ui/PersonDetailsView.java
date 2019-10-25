package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.display.detailwindow.DetailWindowDisplay;
import seedu.address.model.display.detailwindow.WeekSchedule;

/**
 * A class to handle the details view of a person or a group.
 */
public class PersonDetailsView extends UiPart<Region> {

    private static final String FXML = "PersonDetailsView.fxml";

    @FXML
    private StackPane personSchedule;

    @FXML
    private StackPane personDetailContainer;

    public PersonDetailsView(DetailWindowDisplay detailWindowDisplay) {
        super(FXML);
        WeekSchedule schedule = detailWindowDisplay.getWeekSchedules().get(0);
        ScheduleView sv = new ScheduleView(schedule);
        this.personSchedule.getChildren().add(sv.getRoot());
    }

}
