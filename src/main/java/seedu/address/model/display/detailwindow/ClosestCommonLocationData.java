package seedu.address.model.display.detailwindow;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ArrayListUtil;
import seedu.address.websocket.Cache;

/**
 * Data for closest common location
 */
public class ClosestCommonLocationData {
    private boolean isOk = false;
    private String errorResponse;
    private String firstClosest;
    private String secondClosest;
    private String thirdClosest;
    private long firstAvg;
    private long secondAvg;
    private long thirdAvg;
    private ArrayList<String> locationEntered;
    private ArrayList<String> validLocation;
    private ArrayList<String> invalidLocation;
    private final Logger logger = LogsCenter.getLogger(this.getClass());

    public BufferedImage getImage() {
        if (firstClosest.contains("NUS_")) {
            logger.warning(firstClosest + " must not have NUS_ prefix");
        }
        return Cache.loadImage("NUS_" + firstClosest);
    }

    public void setFirstClosest(String firstClosest) {
        this.firstClosest = firstClosest;
    }

    public String getFirstClosest() {
        return firstClosest;
    }

    public void setSecondClosest(String secondClosest) {
        this.secondClosest = secondClosest;
    }

    public String getSecondClosest() {
        return secondClosest;
    }

    public void setThirdClosest(String thirdClosest) {
        this.thirdClosest = thirdClosest;
    }

    public String getThirdClosest() {
        return thirdClosest;
    }

    public String getFirstAvg() {
        return formatAvgForGui(firstAvg);
    }

    public void setFirstAvg(long firstAvg) {
        this.firstAvg = firstAvg;
    }

    public String getSecondAvg() {
        return formatAvgForGui(secondAvg);
    }

    public void setSecondAvg(long secondAvg) {
        this.secondAvg = secondAvg;
    }

    public String getThirdAvg() {
        return formatAvgForGui(thirdAvg);
    }

    public void setThirdAvg(long thirdAvg) {
        this.thirdAvg = thirdAvg;
    }

    public ArrayList<String> getLocationEntered() {
        return locationEntered;
    }

    public void setLocationEntered(ArrayList<String> locationEntered) {
        this.locationEntered = (ArrayList<String>) locationEntered.clone();;
    }

    public ArrayList<String> getValidLocation() {
        return validLocation;
    }

    public void setValidLocation(ArrayList<String> validLocation) {
        this.validLocation = validLocation;
    }

    public ArrayList<String> getInvalidLocation() {
        return invalidLocation;
    }

    public void setInvalidLocation(ArrayList<String> invalidLocation) {
        this.invalidLocation = invalidLocation;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public String getErrorResponse() {

        String response = "";

        response += this.errorResponse + "\n";

        if (!locationEntered.isEmpty()) {
            response += "Source location: " + ArrayListUtil.toStringCommaSpaced(locationEntered) + "\n";
        }

        if (validLocation != null) {
            response += "Valid Source location: " + ArrayListUtil.toStringCommaSpaced(validLocation) + "\n";
        }

        if (invalidLocation != null) {
            response += "Invalid Source location: " + ArrayListUtil.toStringCommaSpaced(invalidLocation) + "\n";
        }

        return response;
    }

    public void setErrorResponse(String errorResponse) {
        this.errorResponse = errorResponse;
    }

    private String formatAvgForGui(long value) {
        return "Avg distance: " + value + "(meters)";
    }

    /**
     * Method to give the String representation of ClosestCommonLocationData.
     */
    public String toString() {
        String result = "";
        result += firstClosest + ": " + firstAvg + "\n";
        result += secondClosest + ": " + secondAvg + "\n";
        result += secondClosest + ": " + thirdAvg + "\n";
        return result;
    }
}
