package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.display.schedulewindow.ScheduleWindowDisplay;
import seedu.address.model.display.schedulewindow.ScheduleWindowDisplayType;
import seedu.address.ui.util.ColorGenerator;

/**
 * Interface to control schedule view in the UI.
 */
public interface ScheduleViewManager {
    public static ScheduleViewManager getInstanceOf(ScheduleWindowDisplay scheduleWindowDisplay) {
        ScheduleWindowDisplayType displayType = scheduleWindowDisplay.getScheduleWindowDisplayType();
        ArrayList<String> colors = ColorGenerator
                .generateColorList();
        switch(displayType) {
        case PERSON:
            //There is only 1 schedule in the scheduleWindowDisplay
            return new IndividualScheduleViewManager(scheduleWindowDisplay.getPersonSchedules(),
                    scheduleWindowDisplay.getPersonSchedules().get(0).get(0).getPersonDisplay(), colors.get(0));
        case GROUP:
            return new GroupScheduleViewManager(scheduleWindowDisplay.getPersonSchedules(),
                    colors,
                    scheduleWindowDisplay.getGroupDisplay().getGroupName(),
                    scheduleWindowDisplay.getFreeSchedule());
        default:
            break;
        }
        return null;
    }
    public ScheduleView getScheduleView();
    public void scrollNext();
    public void toggleNext();
    public List<String> getColors();
    public ScheduleWindowDisplayType getScheduleWindowDisplayType();
    public ScheduleView getScheduleViewCopy();
}
