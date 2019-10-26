package seedu.address.websocket.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class UrlUtilTest {

    @Test
    void sanitizeApiKey() {
        String url = "https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&size"
                + "=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7C"
                + "label:G%7C40.711614,-74.012318&markers=color:red%7Clabel:C%7C40.718217,-73.998284&key=blahblahblah"
                + "blahblahblahblah";
        String expectedUrl = "https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&size"
                + "=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7C"
                + "label:G%7C40.711614,-74.012318&markers=color:red%7Clabel:C%7C40.718217,-73.998284&";
        assertTrue(UrlUtil.sanitizeApiKey(url).equals(expectedUrl));
    }

    @Test
    void generateGmapsPlacesUrl() {
        String expectedUrl = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?location=.sg"
                + "&inputtype=textquery&fields=name,place_id&input=qwerty&key=&";
        assertEquals(UrlUtil.generateGmapsPlacesUrl("qwerty"), expectedUrl);
    }

    @Test
    void generateDistanceMatrixUrl() {
        ArrayList<String> tooLongStringList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            tooLongStringList.add("qwert");
        }
        assertThrows(InvalidParameterException.class, () ->
                UrlUtil.generateGmapsDistanceMatrixUrl(tooLongStringList, tooLongStringList));

        ArrayList<String> arg = new ArrayList<>(Arrays.asList("LT17", "LT13", "LT14"));
        String expectedUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial"
                + "&origins=LT17|LT13|LT14|&destinations=LT17|LT13|LT14|&key=";
        assertEquals(UrlUtil.generateGmapsDistanceMatrixUrl(arg, arg), expectedUrl);
    }

    @Test
    void generateGmapsStaticImage() {
        String expectedUrl = "https://maps.googleapis.com/maps/api/staticmap?size=500x500&zoom=17"
                + "&markers=color:blue|size:large|label:L|NUS_LT17&center=NUS_LT17&key=&";
        assertEquals(UrlUtil.generateGmapsStaticImage("NUS_LT17"), expectedUrl);
    }
}

