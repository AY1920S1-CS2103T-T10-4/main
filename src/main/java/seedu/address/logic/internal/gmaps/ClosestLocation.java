package seedu.address.logic.internal.gmaps;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.ArrayListUtil;
import seedu.address.commons.util.LocationArrayListUtils;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.display.detailwindow.ClosestCommonLocationData;
import seedu.address.model.gmaps.Location;
import seedu.address.model.gmaps.LocationGraph;

/**
 * Class is used to get the closest location
 */
public class ClosestLocation {

    private LocationGraph locationGraph;
    private final Logger logger = LogsCenter.getLogger(this.getClass());
    public ClosestLocation(LocationGraph locationGraph) {
        this.locationGraph = locationGraph;
    }

    /**
     * This is a wrapper function to use internally to get closest location from an ArrayList of venues
     * @param locationNameList
     * @return
     */
    public String closestLocationDataString(ArrayList<String> locationNameList) {
        ClosestCommonLocationData data = closestLocationData(locationNameList);
        String result = "";
        if (data.isOk()) {
            result = result + "\nFirst closest location: " + data.getFirstClosest()
                    + " | Average travelling distance/meters " + data.getFirstAvg() + "\n";
            result = result + "Second closest location: " + data.getSecondClosest()
                    + " | Average travelling distance/meters " + data.getSecondAvg() + "\n";
            result = result + "Third closest location: " + data.getThirdClosest()
                    + " | Average travelling distance/meters " + data.getThirdAvg() + "\n";
            if (!data.getInvalidLocation().isEmpty()) {
                result = result + "Could not recognise these locations:\n"
                        + ArrayListUtil
                        .toStringCommaSpaced(data.getInvalidLocation()) + "\n";
            }
        } else {
            result = "Internal error for " + locationNameList;
        }

        return result;
    }

    /**
     * This method is used to find the closes location from the location graph
     * @return
     */
    public ClosestCommonLocationData closestLocationData(ArrayList<String> locationNameList) {
        requireNonNull(locationNameList);
        ClosestCommonLocationData closestCommonLocationData = new ClosestCommonLocationData();
        closestCommonLocationData.setLocationEntered(locationNameList);
        String firstClosest = null;
        String secondClosest = null;
        String thirdClosest = null;
        ArrayList<String> invalidLocation = new ArrayList<>();
        int groupSize = locationNameList.size();
        try {
            if (locationNameList.isEmpty()) {
                throw new IllegalValueException("You must enter at least one location.");
            }
            ArrayList<ArrayList<Long>> currMatrix = new ArrayList<ArrayList<Long>>();
            ArrayList<Location> validLocationList = locationGraph.getValidLocationList();
            ArrayList<Location> locations = locationGraph.getLocations();
            for (int i = locationNameList.size() - 1; i >= 0; i--) {
                String currLocationString = locationNameList.get(i);
                int validLocationListIndex = -1;
                if (LocationArrayListUtils.containLocationName(locations, currLocationString)) {
                    int locationListIndex = LocationArrayListUtils.getIndex(locations, currLocationString);
                    Location currLocation = locations.get(locationListIndex);
                    validLocationListIndex = LocationArrayListUtils.getIndex(validLocationList,
                            currLocation.getValidLocation());
                } else if (LocationArrayListUtils.containLocationName(validLocationList,
                        "NUS_" + currLocationString)) {
                    validLocationListIndex = LocationArrayListUtils.getIndex(validLocationList,
                            "NUS_" + currLocationString);
                } else {
                    invalidLocation.add(locationNameList.get(i));
                    locationNameList.remove(i);
                    continue;
                }
                ArrayList<Long> currRow = locationGraph.getLocationRow(validLocationListIndex);
                currMatrix.add(currRow);
            }

            closestCommonLocationData.setInvalidLocation(invalidLocation);
            if (currMatrix.isEmpty()) {
                throw new IllegalValueException("All location entered cannot be identified by TimeBook. Refer to  "
                        + "Supported Location table in User Guide to ge the supported locations.");
            }

            ArrayList<Long> totalDistance = new ArrayList<>();

            for (int j = 0; j < currMatrix.get(0).size(); j++) {
                totalDistance.add((long) 0);
                boolean isAllNull = true;
                for (int i = 0; i < locationNameList.size(); i++) {
                    Long currDistance = (long) 0;
                    if (currMatrix.get(i).get(j) != null) {
                        isAllNull = false;
                        currDistance = currMatrix.get(i).get(j);
                    }
                    Long newDistance = totalDistance.get(j) + currDistance;
                    totalDistance.set(j, newDistance);
                }
                if (totalDistance.get(j) == 0 && isAllNull) {
                    totalDistance.set(j, Long.MAX_VALUE);
                }
            }
            int firstClosestIndex = 0;
            int secondClosestIndex = 0;
            int thirdClosestIndex = 0;
            ArrayList<Location> closestLocations = new ArrayList<>();
            Long firstClosestTime = Long.MAX_VALUE;
            Long secondClosestTime = Long.MAX_VALUE;
            Long thirdClosestTime = Long.MAX_VALUE;
            for (int i = 0; i < totalDistance.size(); i++) {
                Location currValidLocation = validLocationList.get(i);
                if (closestLocations.contains(currValidLocation)) {
                    continue;
                }
                if (totalDistance.get(i) < firstClosestTime) {
                    firstClosestIndex = i;
                    firstClosestTime = totalDistance.get(i);
                    closestLocations.add(currValidLocation);
                } else if (totalDistance.get(i) < secondClosestTime) {
                    secondClosestIndex = i;
                    secondClosestTime = totalDistance.get(i);
                    closestLocations.add(currValidLocation);
                } else if (totalDistance.get(i) < thirdClosestTime) {
                    thirdClosestIndex = i;
                    thirdClosestTime = totalDistance.get(i);
                    closestLocations.add(currValidLocation);
                }
            }

            firstClosest = StringUtil.removeNusPrefix(validLocationList.get(firstClosestIndex).getValidLocation());
            secondClosest = StringUtil.removeNusPrefix(validLocationList.get(secondClosestIndex).getValidLocation());
            thirdClosest = StringUtil.removeNusPrefix(validLocationList.get(thirdClosestIndex).getValidLocation());

            long firstClosestAvgTime = firstClosestTime / groupSize;
            long secondClosestAvgTime = secondClosestTime / groupSize;
            long thirdClosestAvgTime = thirdClosestTime / groupSize;

            closestCommonLocationData.setFirstClosest(firstClosest);
            closestCommonLocationData.setSecondClosest(secondClosest);
            closestCommonLocationData.setThirdClosest(thirdClosest);
            closestCommonLocationData.setFirstAvg(firstClosestAvgTime);
            closestCommonLocationData.setSecondAvg(secondClosestAvgTime);
            closestCommonLocationData.setThirdAvg(thirdClosestAvgTime);
            closestCommonLocationData.setValidLocation(locationNameList);
            closestCommonLocationData.setOk(true);
        } catch (IllegalValueException e) {
            closestCommonLocationData.setErrorResponse(e.getLocalizedMessage());
        }
        return closestCommonLocationData;
    }
}
