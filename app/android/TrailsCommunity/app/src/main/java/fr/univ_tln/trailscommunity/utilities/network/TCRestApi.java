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
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * Rest interface API to send and
 * receive data from TrailsCommunity API.
 */
@Rest(rootUrl = "http://trailscommunity.herokuapp.com/api", converters = {MyGsonHttpMessageConverter.class})
@Accept(MediaType.APPLICATION_JSON)
public interface TCRestApi {
    /**
     * Set a specific header to the HTTP request.
     * @param name header name
     * @param value header value
     */
    void setHeader(String name, String value);
    String getHeader(String name);
    /**
     * Login action to authenticate through
     * trailscommunity API.
     *
     * @param formData auth data
     * @return Gson JsonElement object, the response data which
     * contains the auth token to access other resources.
     * <p>
     * ex:
     * {jwt: "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE0ODAzNjk5NDksInN1YiI6Mn0.RuHIw7dcwFyi0ONt3bdam3IxG5O_GAiN2cK4OcSCUJQ"}
     */
    @Post("/login/auth_token")
    @Header(name = "Content-Type", value = "application/json")
    ResponseEntity<JsonObject> login(@Body Map<String, Object> formData);

    @Get("/users/me")
    @RequiresHeader("Authorization")
    ResponseEntity<JsonObject> user();
}
