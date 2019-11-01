package seedu.address.ui.util;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.display.sidepanel.GroupDisplay;
import seedu.address.ui.UiPart;

/**
 * A class to show group details comprehensively.
 */
public class GroupDetailCard extends UiPart<Region> {
    private static final String FXML = "GroupDetailCard.fxml";

    @FXML
    private VBox groupDetailContainer;
    @FXML
    private Label groupName;
    @FXML
    private Label groupDescription;

    public GroupDetailCard(GroupDisplay groupDisplay) {
        super(FXML);
        groupName.setText(groupDisplay.getGroupName().toString());
        groupDescription.setText(groupDisplay.getGroupDescription().toString());
    }
}
