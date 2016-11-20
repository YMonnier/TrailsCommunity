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
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.MediaType;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.androidannotations.rest.spring.api.RestClientHeaders;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.MultiValueMap;

@Rest(rootUrl = "http://trailscommunity.herokuapp.com/api", converters = {MyGsonHttpMessageConverter.class})
public interface TCRestApi extends RestClientErrorHandling {
    @Post("/login/auth_token")
    @RequiresHeader(HttpHeaders.CONTENT_TYPE)
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<JsonElement> login(@Body MultiValueMap<String, Object> formData);
}
