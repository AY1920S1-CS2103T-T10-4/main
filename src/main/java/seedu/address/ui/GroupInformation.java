package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.display.sidepanel.GroupDisplay;
import seedu.address.model.display.sidepanel.PersonDisplay;
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

    public GroupInformation(List<PersonDisplay> members, GroupDisplay groupDisplay, List<String> colors) {
        super(FXML);
        ArrayList<String> names = members.stream().map(p -> p.getName().toString())
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<String> emails = members.stream().map(p -> p.getEmail().toString())
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<String> roles = members.stream().map(p -> p.getRole().toString())
                .collect(Collectors.toCollection(ArrayList::new));

        this.colors = colors;
        GroupDetailCard groupCard = new GroupDetailCard(groupDisplay);
        groupDetails.getChildren().add(groupCard.getRoot());
        groupMembers.getChildren().add(new MemberList(names, emails, roles, colors).getRoot());
    }

}
