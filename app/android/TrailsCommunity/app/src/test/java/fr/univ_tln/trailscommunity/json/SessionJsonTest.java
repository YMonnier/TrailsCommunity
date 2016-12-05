package fr.univ_tln.trailscommunity.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.univ_tln.trailscommunity.models.Session;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.json.
 * File SessionJsonTest.java.
 * Created by Ysee on 05/12/2016 - 16:23.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class SessionJsonTest {
    private int id = 1;
    private int activity = 1;
    private String departurePlace = "48.302176;6.133863";
    private String arrivalPlace = "48.692054;6.184417";
    private String startDate = "2016-11-30";
    private boolean close = false;
    private boolean lock = false;
    private String createAt = "2016-11-28T11:26:17.296Z";
    private String updatedAt = "2016-11-28T11:26:17.296Z";

    private String validSessionJson = "{\n" +
            "    \"session\": {\n" +
            "      \"id\": " + id + ",\n" +
            "      \"activity\": " + activity + ",\n" +
            "      \"departure_place\": \"" + departurePlace + "\",\n" +
            "      \"arrival_place\": \"" + arrivalPlace + "\",\n" +
            "      \"start_date\": \"" + startDate + "\",\n" +
            "      \"close\": " + close + ",\n" +
            "      \"lock\": " + lock + ",\n" +
            "      \"created_at\": \"" + createAt + "\",\n" +
            "      \"updated_at\": \"" + updatedAt + "\"\n" +
            "    }\n" +
            "  }";


    private String invalidSessionJson = "{Bad JSON.....}";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Test valid converting session json to object.
     */
    @Test
    public void validSessionJsonTest() {
        String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";

        Gson gson = new GsonBuilder()
                .setDateFormat(dateFormat)
                .create();

        JsonObject jsonObject = gson.fromJson(validSessionJson, JsonObject.class);
        assertNotNull("Session Json Object should not be null", jsonObject);
        assertTrue("Session Json Object should be a json object", jsonObject.isJsonObject());

        JsonObject sessionJsonObject = jsonObject.getAsJsonObject("session");
        assertNotNull("The sessionJsonObject should not be null", sessionJsonObject);

        Session session = gson.fromJson(sessionJsonObject, Session.class);

        assertNotNull("Session should not be null", session);
        assertEquals("The id should be " + id, session.getId(), id);
        assertEquals("The activity should be " + activity, session.getActivity(), activity);
        assertEquals("The departurePlace should be " + departurePlace, session.getDeparturePlace(), departurePlace);
        assertEquals("The arrivalPlace should be " + arrivalPlace, session.getArrivalPlace(), arrivalPlace);
        assertEquals("The startDate should be " + startDate, session.getStartDate(), startDate);
        assertEquals("The close should be " + close, session.isClose(), close);
        assertEquals("The lock should be " + lock, session.isLock(), lock);
        assertEquals("The createAt should be " + createAt, session.getCreatedAt(), convertWithFormat(dateFormat, createAt));
        assertEquals("The updatedAt should be " + updatedAt, session.getUpdatedAt(), convertWithFormat(dateFormat, updatedAt));
    }

    /**
     * Test valid json with no date converting format.
     * @throws JsonSyntaxException
     */
    @Test
    public void invalidDateParser() throws JsonSyntaxException {
        thrown.expect(JsonSyntaxException.class);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(validSessionJson, JsonObject.class);
        assertNotNull("Session Json Object should not be null", jsonObject);
        assertTrue("Session Json Object should be a json object", jsonObject.isJsonObject());

        JsonObject sessionJsonObject = jsonObject.getAsJsonObject("session");
        assertNotNull("The sessionJsonObject should not be null", sessionJsonObject);
        gson.fromJson(sessionJsonObject, Session.class);
    }


    /**
     * Test invalid converting session json to object.
     */
    @Test
    public void invalidSessionJsonTest() throws JsonSyntaxException {
        //thrown.expect(MalformedJsonException.class);
        thrown.expect(JsonSyntaxException.class);
        Gson gson = new Gson();
        Session session = gson.fromJson(invalidSessionJson, Session.class);

        //assertNotNull("Session should not be null", session);
    }

    /**
     * Convert a string date to date with a specific format.
     *
     * @param format format we ant to return
     * @return a new date converted
     */
    private Date convertWithFormat(String format, String date) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

}
