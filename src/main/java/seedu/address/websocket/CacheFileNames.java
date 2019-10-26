package seedu.address.websocket;

/**
 * Contains names of files defined to be stored in Cache.
 */
public class CacheFileNames {
    /* Cache File Names */
    public static final String CACHE_FOLDER_PATH = "src/main/resources/ApiResponseCache/";
    public static final String GMAPS_FOLDER_PATH = "GoogleMapsApi/";
    public static final String NUSMODS_FOLDER_PATH = "NusModsApi/";

    public static final String MODULES = CACHE_FOLDER_PATH + NUSMODS_FOLDER_PATH + "modules.json";
    public static final String MODULES_SUMMARY = CACHE_FOLDER_PATH + NUSMODS_FOLDER_PATH + "modules_summary.json";
    public static final String HOLIDAYS = CACHE_FOLDER_PATH + NUSMODS_FOLDER_PATH + "holidays.json";
    public static final String ACADEMIC_CALENDAR = CACHE_FOLDER_PATH + NUSMODS_FOLDER_PATH + "academic_calendar.json";

    public static final String GMAPS_IMAGE_DIR = CACHE_FOLDER_PATH + GMAPS_FOLDER_PATH + "GmapsImages/";
    public static final String VENUES_FULL_PATH = CACHE_FOLDER_PATH + NUSMODS_FOLDER_PATH + "Venues.json";
    public static final String GMAPS_DISTANCE_MATRIX_PATH = CACHE_FOLDER_PATH + GMAPS_FOLDER_PATH
            + "DistanceMatrix.json";
    public static final String GMAPS_PLACES_PATH = CACHE_FOLDER_PATH + GMAPS_FOLDER_PATH + "Places.json";
}
