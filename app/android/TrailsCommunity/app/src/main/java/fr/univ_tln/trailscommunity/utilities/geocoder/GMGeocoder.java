package fr.univ_tln.trailscommunity.utilities.geocoder;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import fr.univ_tln.trailscommunity.models.Coordinate;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.geocoder.
 * File GMGeocoder.java.
 * Created by Ysee on 26/11/2016 - 10:31.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class GMGeocoder {
    /**
     * Geocoder helper to convert
     * address to geographical coordinates
     * @param context context use for Geocoder
     * @param address address for converting
     * @return A Coordinate object if the address exists, otherwise, null.
     */
    public static Coordinate addressToCoordinates(Context context, String address) {
        Coordinate position = null;
        if(address == null)
            throw new AssertionError("address should not be null");
        if (address != null) {
            try {
                List<Address> addresses = new Geocoder(context).getFromLocationName(address, 1);
                if(addresses != null) {
                    if (!addresses.isEmpty()) {
                        double latitude = addresses.get(0).getLatitude();
                        double longitude = addresses.get(0).getLongitude();
                        position = new Coordinate.Builder()
                                .setLatitude(latitude)
                                .setLongitude(longitude)
                                .build();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return position;
            }
        }

        return position;
    }
}
