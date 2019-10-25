package seedu.address.ui.util;

import java.time.LocalTime;

/**
 * Class to format time for the UI.
 */
public class TimeFormatter {
    public static int formatTimeToInt(LocalTime localTime) {
        return localTime.getHour() * 100 + localTime.getMinute();
    }

    /**
     * Method to convert int hours to time in 24hours clock representation.
     * @param time Time in hours to be formatted.
     * @return String that represents the time.
     */
    public static String formatIntToTime(int time) {
        if (time == 0) {
            return "0000";
        } else if (time < 10) {
            return "0" + (time * 100);
        } else {
            return time * 100 + "";
        }
    }
}
