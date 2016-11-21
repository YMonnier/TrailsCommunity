package fr.univ_tln.trailscommunity.utilities.network;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.network.
 * File TCRestApi.java.
 * Created by Ysee on 20/11/2016 - 02:18.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

import com.google.gson.JsonElement;

import org.androidannotations.rest.spring.annotations.Accept;
import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Header;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.MediaType;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Rest(rootUrl = "http://trailscommunity.herokuapp.com/api", converters = { MyGsonHttpMessageConverter.class})
public interface TCRestApi {
    @Post("/login/auth_token")
    @Accept(MediaType.APPLICATION_JSON)
    @Header(name = "Content-Type", value = "application/json")
    ResponseEntity<JsonElement> login(@Body Map<String, Object> formData);
}
