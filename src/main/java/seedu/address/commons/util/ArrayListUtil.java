package seedu.address.commons.util;

import java.util.ArrayList;

/**
 * Utility function to manipulate arraylist
 */
public class ArrayListUtil {
    /**
     * Method used to transform an array list to string where each element separated by a ,
     * @param arrayList
     * @return
     */
    public static String toStringCommaSpaced(ArrayList<String> arrayList) {
        String result = "";
        for (int i = 0; i < arrayList.size(); i++) {
            if (i != arrayList.size() - 1) {
                result = result + arrayList.get(i) + ", ";
            } else {
                result = result + arrayList.get(i);
            }
        }
        return result;
    }
}
