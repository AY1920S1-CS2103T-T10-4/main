package seedu.address.websocket;

import java.net.ConnectException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import seedu.address.commons.core.LogsCenter;
import seedu.address.websocket.util.ApiQuery;
import seedu.address.websocket.util.ParserUtil;
import seedu.address.websocket.util.QueryResult;
import seedu.address.websocket.util.UrlUtil;

/**
 * This class is used for making API request to Google Maps API
 */
public class GmapsApi {

    private static final Logger logger = LogsCenter.getLogger(GmapsApi.class);

    public GmapsApi() {
    }

    /**
     * This method is used to execute the Google Maps distance matrix API. It will return
     * a complete matrix mapping of all the String in the locations
     * @param
     * @return JSONObject of api call
     * @throws InvalidParameterException
     * @throws ConnectException
     */
    public static JSONObject getDistanceMatrix(ArrayList<String> locationsRow, ArrayList<String> locationsColumn)
            throws InvalidParameterException, ConnectException {
        ApiQuery query = new ApiQuery(UrlUtil.generateGmapsDistanceMatrixUrl(locationsRow, locationsColumn));
        QueryResult queryResult = query.execute();
        if (queryResult.process(logger)) {
            JSONObject obj = ParserUtil.parseStringToJsonObject(queryResult.getResponseResult());
            return obj;
        } else {
            throw new ConnectException(ParserUtil.parseStringToJsonObject(queryResult.getResponseResult()).toString());
        }
    }

    public static JSONObject getLocation(String locationName) throws ConnectException {
        ApiQuery query = new ApiQuery(UrlUtil.generateGmapsPlacesUrl(locationName));
        QueryResult queryResult = query.execute();
        if (queryResult.process(logger)) {
            JSONObject obj = ParserUtil.parseStringToJsonObject(queryResult.getResponseResult());
            return obj;
        } else {
            throw new ConnectException(ParserUtil.parseStringToJsonObject(queryResult.getResponseResult()).toString());
        }
    }
}
