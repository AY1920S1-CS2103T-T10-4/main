package seedu.address.ui.util;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.display.detailwindow.DetailWindowDisplay;
import seedu.address.model.display.detailwindow.WeekSchedule;
import seedu.address.model.person.ScheduleStub;
import seedu.address.ui.UiPart;

/**
 * Class to handle exportation of group details.
 */
public class GroupDetailsExport extends UiPart<Region> {
    private static final String FXML = "GroupDetailsExport.fxml";

    @FXML
    private StackPane groupDetailCard;

    @FXML
    private StackPane groupMembersList;

    @FXML
    private StackPane groupSchedule;

    @FXML
    private StackPane groupDetailContainer;

    public GroupDetailsExport(DetailWindowDisplay detailWindowDisplay) {
        super(FXML);
        ScheduleStub stub = new ScheduleStub();
        ArrayList<WeekSchedule> schedules = detailWindowDisplay.getWeekSchedules();
        ArrayList<String> names = schedules.stream()
                .map(wkSch -> wkSch.getPersonDisplay().getName().toString())
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<String> emails = schedules.stream()
                .map(wkSch -> wkSch.getPersonDisplay().getEmail().toString())
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<String> roles = detailWindowDisplay.getWeekSchedules().stream()
                .map(wkSch -> wkSch.getRole().toString())
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<String> colors = ColorGenerator.generateColorList(names.size());
        ScheduleExport scheduleView = new ScheduleExport(schedules, colors);
        GroupDetailCard groupCard = new GroupDetailCard(detailWindowDisplay.getGroupDisplay());
        groupDetailCard.getChildren().add(groupCard.getRoot());
        groupMembersList.getChildren().add(new MemberList(names, emails, roles, colors).getRoot());
        groupSchedule.getChildren().add(scheduleView.getScheduleView());
    }
}
