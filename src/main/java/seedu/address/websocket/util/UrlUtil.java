package seedu.address.websocket.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import seedu.address.commons.util.StringUtil;

/**
 * Url Constructor Util
 */
public class UrlUtil {

    private static String gmapsApiKey = "";

    /**
     * Generates a URL object from a String.
     *
     * @param url String to be converted
     * @return URL object or null if URL is malformed
     */
    public static URL generateUrl(String url) {
        try {
            URL query = new URL(url);
            return query;
        } catch (MalformedURLException mue) {
            return null;
        }
    }

    /**
     * THis method is used to remove the API key from the url
     * @param url that contains API key
     * @return url without API key
     */
    public static String sanitizeApiKey(String url) {
        if (url.contains("key=")) {
            return url.split("key=")[0];
        } else {
            return url;
        }
    }

    /**
     * Split query string of URL into a map of key-value pairs. It is assumed that keys are unique.
     * @param url URL to split.
     * @return a map of {@code String} key and {@code String} value.
     */
    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        if (StringUtil.isNullOrEmpty(url.getQuery())) {
            return Collections.emptyMap();
        }

        Map<String, String> queryMap = new LinkedHashMap<>();
        String query = url.getQuery();
        String[] querySplits = query.split("&");

        for (String part : querySplits) {
            int equalsIndex = part.indexOf("=");
            String key = equalsIndex > 0 ? URLDecoder.decode(part.substring(0, equalsIndex), "UTF-8") : part;
            String value = equalsIndex > 0 && part.length() > equalsIndex + 1
                    ? URLDecoder.decode(part.substring(equalsIndex + 1), "UTF-8") : null;
            queryMap.put(key, value);
        }
        return queryMap;
    }

    /**
     * This is a utility function to get the full API endpoint for Gmaps Distance Matrix Api from 2 arraylist of string
     * @param locationsRow
     * @param locationsColumn
     * @return
     */
    public static String generateGmapsDistanceMatrixUrl(
            ArrayList<String> locationsRow, ArrayList<String> locationsColumn) throws InvalidParameterException {
        if (locationsColumn.size() > 10 || locationsRow.size() > 10) {
            throw new InvalidParameterException("GMAPS API Only can make request to 10 locations.");
        }
        String baseUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&";
        String apiKeyQueryParams = "key=" + gmapsApiKey;
        String originQueryParams = "origins=";
        String destinationQueryParams = "destinations=";
        for (int i = 0; i < locationsRow.size(); i++) {
            originQueryParams = originQueryParams + locationsRow.get(i) + "|";
        }
        for (int i = 0; i < locationsColumn.size(); i++) {
            destinationQueryParams = destinationQueryParams + locationsColumn.get(i) + "|";
        }
        originQueryParams = originQueryParams + "&";
        destinationQueryParams = destinationQueryParams + "&";
        String fullUrl = baseUrl + originQueryParams + destinationQueryParams + apiKeyQueryParams;
        return fullUrl;
    }

    /**
     * This is a utility function to get the full API endpoint for Gmaps places Api from a string
     * @param locationName
     * @return
     */
    public static String generateGmapsPlacesUrl(String locationName) {
        String baseUrl = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?location=.sg&"
                + "inputtype=textquery&fields=name,place_id&";
        String apiKeyQueryParams = "key=" + gmapsApiKey + "&";
        String queryParams = "input=" + locationName + "&";
        String fullUrl = baseUrl + queryParams + apiKeyQueryParams;
        return fullUrl;
    }

    /**
     * This method is used to get image from GMAPS and save to local directory
     * @param validLocationName
     * @return
     */
    public static String generateGmapsStaticImage(String validLocationName) {
        String baseUrl = "https://maps.googleapis.com/maps/api/staticmap?size=500x500&zoom=17";
        String markerQueryParam = "&markers=color:blue|size:large|label:L|" + validLocationName;
        String centerQueryParam = "&center=" + validLocationName;
        String apiKeyQueryParams = "&key=" + gmapsApiKey + "&";
        String fullUrl = baseUrl + markerQueryParam + centerQueryParam + apiKeyQueryParams;
        return fullUrl;
    }
}
