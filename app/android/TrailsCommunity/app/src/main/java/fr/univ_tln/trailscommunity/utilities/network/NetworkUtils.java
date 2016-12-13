package fr.univ_tln.trailscommunity.utilities.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.network.
 * File NetworkUtils.java.
 * Created by Ysee on 10/12/2016 - 16:39.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class NetworkUtils {
    /**
     * Allowing to know if the phone
     * is connected to a internet network.
     * @param context source activity
     * @return true id connected to the internet network, otherwise false.
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
