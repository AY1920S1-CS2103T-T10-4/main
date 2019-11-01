package seedu.address.ui.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to handle generation of colors.
 */
public class ColorGenerator {
    private static ArrayList<String> listOfColors = new ArrayList<String>(List.of("#FF8A80", "#F06292",
            "#B388FF", "#8C9EFF", "#18FFFF", "#64FFDA", "#69F0AE", "#FFC400", "#FFFF00", "#FFAB40",
            "#FF9E80", "#90A4AE", "#BCAAA4", "66FCF1", "#FF80AB", "#EA80FC", "#80D8FF", "#00E676"));

    /**
     * Method to generate a random list of colors to tag each group member with a particular colour.
     * @return A list of unique colors for the group's schedule view.
     */
    public static ArrayList<String> generateColorList() {
        return listOfColors;
    }
}
