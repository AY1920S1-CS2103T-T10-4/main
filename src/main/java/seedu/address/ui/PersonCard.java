package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.display.sidepanel.PersonDisplay;
import seedu.address.ui.util.BubbleGenerator;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final PersonDisplay person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private StackPane personId;
    @FXML
    private FlowPane tags;

    public PersonCard(PersonDisplay person) {
        super(FXML);
        this.person = person;
        String personName = person.getName().fullName;
        String personInitials = getPersonInitials(personName);
        personId.getChildren().add(new BubbleGenerator(personInitials, 50).getBubble());
        name.setText(personName);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return person.isSamePerson(card.person);
    }

    public static String getPersonInitials(String personName) {
        String[] fragmentedNames = personName.toUpperCase().split(" ");
        return fragmentedNames[0].charAt(0) + "";
    }
}
