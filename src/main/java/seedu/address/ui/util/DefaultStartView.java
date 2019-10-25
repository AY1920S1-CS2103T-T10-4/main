package seedu.address.ui.util;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.ui.UiPart;

/**
 * A class to show our projet logo when the application starts up.
 */
public class DefaultStartView extends UiPart<Region> {

    private static final String FXML = "DefaultStartView.fxml";
    private final Image logo = new Image(getClass().getResourceAsStream("/images/timebook_logo.png"));
    @FXML
    private ImageView logoView;

    @FXML
    private StackPane defaultStartViewContainer;

    public DefaultStartView(double prefWidth, double prefHeight) {
        super(FXML);
        logoView.setImage(logo);
        logoView.setFitHeight(prefHeight);
        logoView.setFitWidth(prefWidth);
        logoView.setPreserveRatio(true);
    }
}
