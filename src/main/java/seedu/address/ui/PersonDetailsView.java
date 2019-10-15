package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.display.detailwindow.DetailWindowDisplay;
import seedu.address.model.display.detailwindow.WeekSchedule;
import seedu.address.ui.util.ColorGenerator;

/**
 * A class to handle the details view of a person or a group.
 */
public class PersonDetailsView extends UiPart<Region> {

    private static final String FXML = "PersonDetailsView.fxml";

    @FXML
    private StackPane personDetailCard;

    @FXML
    private StackPane personSchedule;

    @FXML
    private StackPane personDetailContainer;

    public PersonDetailsView(DetailWindowDisplay detailWindowDisplay) {
        super(FXML);
        WeekSchedule schedule = detailWindowDisplay.getWeekSchedules().get(0);
        PersonDetailCard personDetailCard = new PersonDetailCard(schedule.getPersonDisplay());
        ScheduleView sv = new ScheduleView(detailWindowDisplay.getWeekSchedules(),
                ColorGenerator.generateColorList(detailWindowDisplay.getWeekSchedules().size()));
        this.personDetailCard.getChildren().add(personDetailCard.getRoot());
        this.personSchedule.getChildren().add(sv.getRoot());
    }

}
