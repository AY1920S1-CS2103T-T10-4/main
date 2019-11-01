package seedu.address.logic.export;

import java.io.IOException;

import javafx.scene.Scene;
import seedu.address.ui.ScheduleView;

/**
 * Class to handle exporting visual representation of an individual.
 * Handles the exportation of an individual's schedule.
 */
public class IndividualScheduleExporter implements Exporter {
    private ScheduleView scheduleView;
    private String fileFormat;
    private String filepath;

    public IndividualScheduleExporter(ScheduleView scheduleView, String fileFormat, String filepath) {
        this.scheduleView = scheduleView;
        this.fileFormat = fileFormat;
        this.filepath = filepath;
    }

    /**
     * Method to export this individual's schedule to a png file.
     * @throws IOException to be handled elsewhere.
     */
    public void export() throws IOException {
        Scene scene = new Scene(scheduleView.getRoot());
        scene.getStylesheets().add("/view/DarkTheme.css");
        VisualExporter.exportTo(scene.getRoot(), fileFormat, filepath);
    }
}
