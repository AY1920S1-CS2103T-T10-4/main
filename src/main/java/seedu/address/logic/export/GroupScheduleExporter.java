package seedu.address.logic.export;

import java.io.IOException;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import seedu.address.ui.GroupInformation;
import seedu.address.ui.ScheduleView;

/**
 * Class to handle exporting visual representation of a group.
 * Details of the group and the schedules of the members are exported to a png file.
 */
public class GroupScheduleExporter implements Exporter {
    private ScheduleView scheduleView;
    private GroupInformation groupInformation;
    private String fileFormat;
    private String filepath;

    public GroupScheduleExporter(ScheduleView scheduleView, GroupInformation groupInformation,
                                 String fileFormat, String filepath) {
        this.scheduleView = scheduleView;
        this.fileFormat = fileFormat;
        this.filepath = filepath;
        this.groupInformation = groupInformation;
    }

    /**
     * Method to export this group's schedule to a png file.
     * @throws IOException to be handled elsewhere.
     */
    public void export() throws IOException {
        HBox exportContainer = new HBox();
        exportContainer.getChildren().addAll(groupInformation.getRoot(), scheduleView.getRoot());
        exportContainer.setId("exportContainer");
        Scene scene = new Scene(exportContainer);
        scene.getStylesheets().add("/view/DarkTheme.css");
        VisualExporter.exportTo(scene.getRoot(), fileFormat, filepath);
    }
}
