package seedu.address.model.display.detailwindow;

import java.util.ArrayList;

import seedu.address.model.display.sidepanel.GroupDisplay;

/**
 * Main window display model.
 */
public class DetailWindowDisplay {

    private ArrayList<WeekSchedule> weekSchedules;
    private DetailWindowDisplayType detailWindowDisplayType;
    private GroupDisplay groupDisplay;

    private FreeSchedule freeSchedule;

    public DetailWindowDisplay(ArrayList<WeekSchedule> weekSchedules, DetailWindowDisplayType detailWindowDisplayType,
                               GroupDisplay groupDisplay) {
        this.detailWindowDisplayType = detailWindowDisplayType;
        this.weekSchedules = weekSchedules;
        this.groupDisplay = groupDisplay;

        this.freeSchedule = new FreeSchedule(this.weekSchedules);

    }

    public DetailWindowDisplay(ArrayList<WeekSchedule> weekSchedules, DetailWindowDisplayType detailWindowDisplayType) {
        this.detailWindowDisplayType = detailWindowDisplayType;
        this.weekSchedules = weekSchedules;
        this.groupDisplay = null;

        this.freeSchedule = new FreeSchedule(this.weekSchedules);
    }

    public DetailWindowDisplay() {
        this.weekSchedules = new ArrayList<>();
        this.detailWindowDisplayType = DetailWindowDisplayType.DEFAULT;
        this.groupDisplay = null;

        this.freeSchedule = null;
    }

    public DetailWindowDisplay(DetailWindowDisplayType type) {
        this.weekSchedules = new ArrayList<>();
        this.detailWindowDisplayType = type;
        this.groupDisplay = null;

        this.freeSchedule = null;
    }

    public DetailWindowDisplayType getDetailWindowDisplayType() {
        return detailWindowDisplayType;
    }

    public ArrayList<WeekSchedule> getWeekSchedules() {
        return weekSchedules;
    }

    public GroupDisplay getGroupDisplay() {
        return groupDisplay;
    }

    public FreeSchedule getFreeSchedule() {
        return freeSchedule;
    }
}
