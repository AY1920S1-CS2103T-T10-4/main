package seedu.address.ui.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A class to handle generation of colors.
 */
public class ColorGenerator {
    private static ArrayList<String> listOfColors = new ArrayList<String>(List.of("#FFA8A8", "#FFCA9E", "#FBFF6A",
            "#7FFFFE", "#8B7FFF", "#DB7FFF", "#FFA5E5"));

    /**
     * Method to generate a random list of colors to tag each group member with a particular colour.
     * @param groupSize The group size.
     * @return A list of unique colors for the group's schedule view.
     */
    public static ArrayList<String> generateColorList(int groupSize) {
        ArrayList<String> colors = new ArrayList<String>();
        HashSet<String> colorChecker = new HashSet<String>();
        int i = 0;
        while (i < groupSize) {
            int index = (int) (Math.random() * (listOfColors.size() - 1));
            if (colorChecker.contains(listOfColors.get(index))) {
                continue;
            }
            colors.add(listOfColors.get(index));
            colorChecker.add(listOfColors.get(index));
            i++;
        }
        return colors;
    }
}
