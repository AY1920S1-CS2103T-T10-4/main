package seedu.address.model.display.detailwindow;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import seedu.address.websocket.CacheFileNames;

/**
 * Data for closest common location
 */
public class ClosestCommonLocationData {
    private boolean isOk = false;
    private String imagePath = null;
    private String firstClosest;
    private String secondClosest;
    private String thirdClosest;
    private long firstAvg;
    private long secondAvg;
    private long thirdAvg;
    private ArrayList<String> invalidLocation;

    public String getImagePath() {
        return imagePath;
    }

    public String getFirstClosest() {
        return firstClosest;
    }

    public String getSecondClosest() {
        return secondClosest;
    }

    public String getThirdClosest() {
        return thirdClosest;
    }

    public long getFirstAvg() {
        return firstAvg;
    }

    public long getSecondAvg() {
        return secondAvg;
    }

    public long getThirdAvg() {
        return thirdAvg;
    }

    public ArrayList<String> getInvalidLocation() {
        return invalidLocation;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setImagePath(String locationName) {
        String tempPath = CacheFileNames.GMAPS_IMAGE_DIR + locationName + ".png";
        try (Reader reader = new FileReader(tempPath)) {
            imagePath = tempPath;
        } catch (IOException e) {
            System.out.println(locationName + " picture is not available");
        }
    }

    public void setFirstClosest(String firstClosest) {
        this.firstClosest = firstClosest;
    }

    public void setSecondClosest(String secondClosest) {
        this.secondClosest = secondClosest;
    }

    public void setThirdClosest(String thirdClosest) {
        this.thirdClosest = thirdClosest;
    }

    public void setFirstAvg(long firstAvg) {
        this.firstAvg = firstAvg;
    }

    public void setSecondAvg(long secondAvg) {
        this.secondAvg = secondAvg;
    }

    public void setThirdAvg(long thirdAvg) {
        this.thirdAvg = thirdAvg;
    }

    public void setInvalidLocation(ArrayList<String> invalidLocation) {
        this.invalidLocation = invalidLocation;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }
}
