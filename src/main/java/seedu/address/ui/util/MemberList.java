package seedu.address.ui.util;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.ui.UiPart;

/**
 * A class to show the members of a group.
 */
public class MemberList extends UiPart<Region> {

    private static final String FXML = "MemberList.fxml";

    @FXML
    private GridPane listHeaders;

    @FXML
    private VBox listMembers;

    @FXML
    private ScrollPane listHeaderContainer;

    @FXML
    private ScrollPane listMemberContainer;

    public MemberList(List<String> members, List<String> emails, List<String> roles, List<String> colors) {
        super(FXML);
        for (int i = 0; i < members.size(); i++) {
            MemberCard memberCard = new MemberCard(members.get(i), emails.get(i), roles.get(i), colors.get(i));
            listMembers.getChildren().add(memberCard.getRoot());
        }
        listHeaderContainer.hvalueProperty().bindBidirectional(listMemberContainer.hvalueProperty());
        listMemberContainer.setFitToWidth(true);
        listHeaderContainer.setFitToWidth(true);
    }

    /**
     * A subclass of MemberList to show MemberCard of group members.
     */
    class MemberCard extends UiPart<Region> {
        private static final String FXML = "MemberCard.fxml";

        @FXML
        private GridPane memberCard;
        @FXML
        private Label memberName;
        @FXML
        private Label memberEmail;
        @FXML
        private Label memberRole;
        @FXML
        private Region colorRegion;

        public MemberCard(String name, String email, String role, String color) {
            super(FXML);

            this.memberName.setText(name);

            if (email.equals("")) {
                this.memberEmail.setText("No Email Available");
            } else {
                this.memberEmail.setText(email);
            }

            if (role.equals("")) {
                this.memberRole.setText("No Role Available");
            } else {
                this.memberRole.setText(role);
            }

            setUpCard(color);
        }

        private GridPane setUpCard(String color) {
            DropShadow shadowEffect = new DropShadow();
            shadowEffect.setOffsetY(3);
            shadowEffect.setOffsetX(3);
            memberCard.setEffect(shadowEffect);
            colorRegion.setStyle("-fx-background-color:" + color + ";"
                    + "-fx-border-radius: 2;"
                    + "-fx-background-radius: 2;"
                    + "-fx-min-height: 50;");
            return memberCard;
        }
    }
}
