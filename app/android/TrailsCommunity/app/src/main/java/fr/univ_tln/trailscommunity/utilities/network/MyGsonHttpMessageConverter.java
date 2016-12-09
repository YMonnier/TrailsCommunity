package fr.univ_tln.trailscommunity.utilities.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.util.Arrays;
import java.util.List;

import fr.univ_tln.trailscommunity.utilities.json.GsonSingleton;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.network.
 * File MyGsonHttpMessageConverter.java.
 * Created by Ysee on 20/11/2016 - 17:48.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class MyGsonHttpMessageConverter extends GsonHttpMessageConverter {
    /**
     * Override MediaType for GsonHttpMessageConverter
     */
    public MyGsonHttpMessageConverter() {
        List<MediaType> types = Arrays.asList(
                new MediaType("text", "html", DEFAULT_CHARSET),
                new MediaType("application", "json", DEFAULT_CHARSET),
                new MediaType("application", "*+json", DEFAULT_CHARSET)
        );

        Gson customGson = new GsonBuilder()
                .setDateFormat(GsonSingleton.DATE_FORMAT)
                .create();

        super.setGson(customGson);
        super.setSupportedMediaTypes(types);
    }
}
