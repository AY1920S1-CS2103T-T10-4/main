package seedu.address.logic.internal.gmaps;

import java.util.ArrayList;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.TimeBookInvalidLocation;
import seedu.address.commons.util.ArrayListUtil;
import seedu.address.model.display.detailwindow.ClosestCommonLocationData;
import seedu.address.model.gmaps.Location;
import seedu.address.model.gmaps.LocationGraph;

/**
 * Class is used to get the closest location
 */
public class ClosestLocation {

    private LocationGraph locationGraph;

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

        return result;
    }

    /**
     * This method is used to find the closes location from the location graph
     * @return
     */
    public ClosestCommonLocationData closestLocationData(ArrayList<String> locationNameList) {
        ClosestCommonLocationData closestCommonLocationData = new ClosestCommonLocationData();
        String firstClosest = null;
        String secondClosest = null;
        String thirdClosest = null;
        ArrayList<String> invalidLocation = new ArrayList<>();
        int groupSize = locationNameList.size();
        try {
            if (locationNameList.isEmpty()) {
                throw new TimeBookInvalidLocation("No location entered");
            }
            ArrayList<ArrayList<Long>> currMatrix = new ArrayList<ArrayList<Long>>();
            ArrayList<String> validLocationList = locationGraph.getValidLocationList();
            ArrayList<Location> locations = locationGraph.getLocations();
            for (int i = 0; i < locationNameList.size(); i++) {
                int indexLocation = LocationArrayListUtils.getIndex(locations, locationNameList.get(i));
                String validLocation = null;
                if (indexLocation >= 0) {
                    Location currLocation = locations.get(indexLocation);
                    validLocation = currLocation.getValidLocation();
                } else {
                    if (validLocationList.contains("NUS_" + locationNameList.get(i))) {
                        validLocation = locationNameList.get(i);
                    }
                }
                if (validLocation != null) {
                    int indexGmapsRecognisedLocation = validLocationList.indexOf(validLocation);
                    if (indexGmapsRecognisedLocation == -1) {
                        indexGmapsRecognisedLocation = validLocationList.indexOf("NUS_" + validLocation);
                    }
                    System.out.println("Index of " + validLocation + " is " + indexGmapsRecognisedLocation);
                    ArrayList<Long> currRow = locationGraph.getLocationRow(indexGmapsRecognisedLocation);
                    currMatrix.add(currRow);
                } else {
                    invalidLocation.add(locationNameList.get(i));
                    locationNameList.remove(i);
                }
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
            Long firstClosestTime = Long.MAX_VALUE;
            Long secondClosestTime = Long.MAX_VALUE;
            Long thirdClosestTime = Long.MAX_VALUE;
            for (int i = 0; i < totalDistance.size(); i++) {
                if (totalDistance.get(i) < firstClosestTime) {
                    firstClosestIndex = i;
                    firstClosestTime = totalDistance.get(i);
                } else if (totalDistance.get(i) < secondClosestTime) {
                    secondClosestIndex = i;
                    secondClosestTime = totalDistance.get(i);
                } else if (totalDistance.get(i) < thirdClosestTime) {
                    secondClosestIndex = i;
                    thirdClosestTime = totalDistance.get(i);
                }
            }

            firstClosest = removeNusPrefix(validLocationList.get(firstClosestIndex));
            secondClosest = removeNusPrefix(validLocationList.get(secondClosestIndex));
            thirdClosest = removeNusPrefix(validLocationList.get(thirdClosestIndex));

            long firstClosestAvgTime = firstClosestTime / groupSize;
            long secondClosestAvgTime = secondClosestTime / groupSize;
            long thirdClosestAvgTime = thirdClosestTime / groupSize;

            closestCommonLocationData.setImagePath(firstClosest);
            closestCommonLocationData.setFirstClosest(firstClosest);
            closestCommonLocationData.setSecondClosest(secondClosest);
            closestCommonLocationData.setThirdClosest(thirdClosest);
            closestCommonLocationData.setFirstAvg(firstClosestAvgTime);
            closestCommonLocationData.setSecondAvg(secondClosestAvgTime);
            closestCommonLocationData.setThirdAvg(thirdClosestAvgTime);
            closestCommonLocationData.setInvalidLocation(invalidLocation);
            closestCommonLocationData.setOk(true);
        } catch (IllegalValueException | TimeBookInvalidLocation e) {
            e.printStackTrace();
        }
        return closestCommonLocationData;
    }

    /**
     * This method is used to remove the Nus Prefix in the validLocationName
     * @param locationName
     * @return
     */
    private static String removeNusPrefix(String locationName) {
        if (locationName.contains("NUS_")) {
            return locationName.split("NUS_")[1];
        } else {
            System.out.println("Weird result " + locationName);
            return locationName;
        }
    }
}
