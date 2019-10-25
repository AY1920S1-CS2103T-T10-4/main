package seedu.address.ui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * UI class to show suggested location details.
 */
public class LocationDetails extends UiPart<Region> {
    private static final String FXML = "LocationDetails.fxml";

    @FXML
    private GridPane locationDetailsContainer;

    @FXML
    private StackPane locationMap;

    @FXML
    private Label locationName;

    @FXML
    private Label timeslot;

    @FXML
    private Label approxTravelTime;

    public LocationDetails(ImageView mapView, List<String> details) {
        super(FXML);
        locationMap.getChildren().add(mapView);
        locationName.setText("Location placeholder");
        timeslot.setText("Timeslot placeholder");
        approxTravelTime.setText("Travel time placeholder");
    }
}
