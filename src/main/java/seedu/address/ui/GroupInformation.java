package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.display.schedulewindow.ScheduleWindowDisplay;
import seedu.address.model.display.schedulewindow.MonthSchedule;
import seedu.address.model.display.schedulewindow.WeekSchedule;
import seedu.address.ui.util.GroupDetailCard;
import seedu.address.ui.util.MemberList;

/**
 * A class to handle the view of a group.
 */
public class GroupInformation extends UiPart<Region> {

    private static final String FXML = "GroupInformation.fxml";

    @FXML
    private StackPane groupDetails;

    @FXML
    private StackPane groupMembers;

    private List<String> colors;

    public GroupInformation(ScheduleWindowDisplay scheduleWindowDisplay, List<String> colors) {
        super(FXML);
        List<WeekSchedule> wkschds = MonthSchedule.getWeekSchedulesForWeek(scheduleWindowDisplay.getMonthSchedules(), 0);
        ArrayList<String> names = wkschds.stream()
                .map(wkSch -> wkSch.getPersonDisplay().getName().toString())
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<String> emails = wkschds.stream()
                .map(wkSch -> wkSch.getPersonDisplay().getEmail().toString())
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<String> roles = wkschds.stream()
                .map(wkSch -> wkSch.getRole().toString())
                .collect(Collectors.toCollection(ArrayList::new));

        this.colors = colors;
        GroupDetailCard groupCard = new GroupDetailCard(scheduleWindowDisplay.getGroupDisplay());
        groupDetails.getChildren().add(groupCard.getRoot());
        groupMembers.getChildren().add(new MemberList(names, emails, roles, colors).getRoot());
    }

}
