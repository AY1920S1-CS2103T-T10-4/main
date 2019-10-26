package seedu.address.websocket.util;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.JSONParser;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.websocket.CacheFileNames;

/**
 * This class is used to get an image from a api response
 */
public class ImageQuery {
    /**
     * This mthod checks if the API response is valid then saves it into the directory
     * @param imageUrl
     * @param filePath
     */
    public static void execute(String imageUrl, String filePath) {
        try {
            if (isValid(imageUrl)) {
                saveImage(imageUrl, filePath);
            } else {
                throw new IllegalValueException(imageUrl + "Not identifiable by gmaps");
            }
        } catch (IllegalValueException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *This method is used to check is the API response is valid.
     * @param imageUrl
     * @return
     */
    private static boolean isValid(String imageUrl) {
        boolean isValid = true;
        try {
            URL url = new URL(imageUrl);
            URLConnection conn = url.openConnection();

            Map<String, List<String>> map = conn.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String key = entry.getKey();
                if (key != null && key.equals("X-Staticmap-API-Warning")) {
                    isValid = false;
                    JSONParser parser;
                    parser = new JSONParser();
                    String name = imageUrl.split("center=")[1].split("&")[0].split("NUS_")[1];
                    try {
                        Reader reader = new FileReader(CacheFileNames.GMAPS_IMAGE_DIR + name + ".png");
                        System.out.println(name + " not available on gmaps but manually added");
                    } catch (IOException e) {
                        System.out.println(name + " not on gmaps and not added");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    /**
     * This method is used to save an image from api response
     * @param imageUrl
     * @param destinationFile
     * @throws IOException
     */
    private static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }
}
