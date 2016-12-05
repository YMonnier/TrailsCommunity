package fr.univ_tln.trailscommunity.utilities.network;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.network.
 * File TCRestApi.java.
 * Created by Ysee on 20/11/2016 - 02:18.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

import com.google.gson.JsonObject;

import org.androidannotations.rest.spring.annotations.Accept;
import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Header;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import fr.univ_tln.trailscommunity.models.Coordinate;
import fr.univ_tln.trailscommunity.models.Session;

/**
 * Rest interface API to send and
 * receive data from TrailsCommunity API.
 */
@Rest(rootUrl = "http://trailscommunity.herokuapp.com/api", converters = {MyGsonHttpMessageConverter.class})
@Accept(MediaType.APPLICATION_JSON)
public interface TCRestApi {
    /**
     * Set a specific header to the HTTP request.
     *
     * @param name  header name
     * @param value header value
     */
    void setHeader(String name, String value);

    String getHeader(String name);

    /**
     * Login action to authenticate through
     * trailscommunity API.
     *
     * @param formData auth data
     * @return Gson JsonObject object, the response data which
     * contains the auth token to access other resources.
     * <p>
     * ex:
     * {jwt: "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE0ODAzNjk5NDksInN1YiI6Mn0.RuHIw7dcwFyi0ONt3bdam3IxG5O_GAiN2cK4OcSCUJQ"}
     */
    @Post("/login/auth_token")
    @Header(name = "Content-Type", value = "application/json")
    ResponseEntity<JsonObject> login(@Body Map<String, Object> formData);

    /**
     * Action which allows to get current user information.
     *
     * @return Gson JsonObject object, the response data
     * which contains the current user information.
     * <p>
     * ex:
     * {"data":{
     * "id":2,
     * "nickname":"Test",
     * "phone_number":"+33651678908",
     * "current_session_id":null,
     * "password_digest":"$2a$10$qS3Z13wxzxYPcZPIT07FdOsFsLXKAldBxslBqDTuXnOoamG8xWOne"
     * }
     * }
     */
    @Get("/users/me")
    @RequiresHeader("Authorization")
    ResponseEntity<JsonObject> user();

    @Get("/users/me?device_token={deviseToken}")
    @RequiresHeader("Authorization")
    ResponseEntity<JsonObject> user(@Path("deviseToken") String deviceToken);


    /**
     * Action which allows to create a new user.
     * <p>
     * ex:
     * {
     * "nickname": "Arthur83",
     * "email": "apaul@custom.com",
     * "phone_number": "+33651678908",
     * "password": "myPassword33",
     * "password_confirmation": "myPassword33"
     * }
     *
     * @param formData map containing user registration information
     * @return response from server
     */
    @Post("/users")
    @Header(name = "Content-Type", value = "application/json")
    ResponseEntity<JsonObject> registration(@Body Map<String, String> formData);

    /**
     * Get all sessions from the TrailsCommunity API.
     * These sessions are already sort in 3 categories.
     * 1. Active session (Actives session, not close by the creator)
     * 2. My sessions (session of the current user)
     * 3. History (session that the user participated and closed)
     *
     * @return list of sessions.
     * <p>
     * ex:
     */
    @Get("/sessions")
    @RequiresHeader("Authorization")
    ResponseEntity<JsonObject> sessions();

    /**
     * Create a new session to the TrailsCommunity API.
     * Body Parameters:
     * * the password can be optional
     * {
     * "password": "sessionLocked",
     * "activity": 10,
     * "departure_place": "43.179363;5.717782",
     * "arrival_place": "43.191168;5.730819",
     * "start_date": "2016-11-20"
     * }
     *
     * @param session session object parameters
     * @return status code 201 if OK, otherwise, 4xx, 5xx status code.
     */
    @Post("/sessions")
    @Header(name = "Content-Type", value = "application/json")
    @RequiresHeader("Authorization")
    ResponseEntity<JsonObject> createSession(@Body Session session);


    @Get("/sessions/{id}/join")
    @RequiresHeader("Authorization")
    ResponseEntity<JsonObject> joinSession(@Path int id);

    @Get("/sessions/{id}/join?password={password}")
    @RequiresHeader("Authorization")
    ResponseEntity<String> joinSession(@Path("id") int id, @Path("password") String password);

    /**
     * Create a new waypoint and broadcast it via Push Notification.
     *
     * @param id         session id
     * @param coordinate waypoint location
     * @return status code 200 if OK, otherwise, 4xx, 5xx status code.
     */
    @Post("/sessions/{id}/waypoint")
    @Header(name = "Content-Type", value = "application/json")
    @RequiresHeader("Authorization")
    void shareWaypoint(@Path("id") int id, @Body Coordinate coordinate);


    /**
     * Create a new user location and broadcast it via Push Notification.
     *
     * @param id         session id
     * @param coordinate user location
     * @return status code 200 if OK, otherwise, 4xx, 5xx status code.
     */
    @Post("/sessions/{id}/coordinate")
    @Header(name = "Content-Type", value = "application/json")
    @RequiresHeader("Authorization")
    void shareCoordinate(@Path("id") int id, @Body Coordinate coordinate);
}
