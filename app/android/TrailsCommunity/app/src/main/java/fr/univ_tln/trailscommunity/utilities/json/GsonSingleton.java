package fr.univ_tln.trailscommunity.utilities.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.json.
 * File GsonSingleton.java.
 * Created by Ysee on 09/12/2016 - 13:23.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class GsonSingleton {
    private static Gson gson;
    public final static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Singleton pattern,
     *
     * @return gson instance.
     */
    public static Gson getInstance() {
        if (gson == null) {
            return new GsonBuilder()
                    .setDateFormat(DATE_FORMAT)
                    .create();
        }
        if (gson == null)
            throw new AssertionError("The gson instance should noy be null");
        return gson;
    }
}
