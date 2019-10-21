package seedu.address.logic.internal.gmaps;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import seedu.address.commons.exceptions.TimeBookInvalidState;
import seedu.address.model.gmaps.LocationGraph;
import seedu.address.websocket.GmapsApi;

/**
 * This method is used to initialise the location graph method
 */
public class ProcessLocationGraph {
    private GmapsApi gmapsApi = new GmapsApi();
    private LocationGraph locationGraph;

    public ProcessLocationGraph(LocationGraph locationGraph) {
        this.locationGraph = locationGraph;
    }

    private void setMatrixRows(ArrayList<ArrayList<Long>> distanceMatrix, int start, int end)
            throws ConnectException, TimeBookInvalidState {
        if (distanceMatrix.size() != end - start + 1) {
            System.out.println((distanceMatrix.size() + "|" + start + "|" + end));
            System.out.println(distanceMatrix);
            throw new InvalidParameterException("distanceMatrix size must equal to start - end + 1");
        } else {
            for (int i = 0; i < distanceMatrix.size(); i++) {
                ArrayList<Long> currRow = distanceMatrix.get(i);
                locationGraph.setMatrixRow(i + start, currRow);
            }
        }
    }

    /**
     * This method is used to populate the distance matrix.
     * @throws ConnectException
     */
    public void populateMatrix() throws ConnectException, TimeBookInvalidState {
        ProcessLocationGraph processLocationGraph = this;
        System.out.println("Start populating");
        ArrayList<String> gmapsRecognisedLocationList = locationGraph.getValidLocationList();
        for (int i = 0; i <= gmapsRecognisedLocationList.size() / 10; i++) {
            ArrayList<String> locationRowString = new ArrayList<String>(gmapsRecognisedLocationList
                    .subList(i * 10 , Math.min((i + 1) * 10, gmapsRecognisedLocationList.size())));
            for (int j = 0; j < gmapsRecognisedLocationList.size() / 10; j++) {
                System.out.println("Processing row" + i + "/" + ((gmapsRecognisedLocationList.size() / 10) + 1));
                System.out.println("Processing cloumn" + j + "/" + ((gmapsRecognisedLocationList.size() / 10) + 1));
                ArrayList<String> locationColumnString = new ArrayList<String>(gmapsRecognisedLocationList
                        .subList(j * 10 , Math.min((j + 1) * 10, gmapsRecognisedLocationList.size())));
                JSONObject apiResponse = gmapsApi.getDistanceMatrix(locationRowString, locationColumnString);
                ArrayList<ArrayList<Long>> currMatrix = GmapsJsonUtils.getArrayListMatrix(apiResponse);
                setMatrixRows(currMatrix, i * 10, Math.min(i * 10 + 9, gmapsRecognisedLocationList.size() - 1));
            }
        }
        System.out.println("Finish populating");
    }

    /**
     * This method is used to save the instance of the LocationGraph
     */
    public void saveLocationGraph() {
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream("data/LocationGraphSerialization");
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(locationGraph);

            out.close();
            file.close();

            System.out.println("LocationGraph has been serialized");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
