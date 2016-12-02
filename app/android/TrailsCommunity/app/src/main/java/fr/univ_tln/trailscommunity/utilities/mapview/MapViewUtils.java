package fr.univ_tln.trailscommunity.utilities.mapview;

import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.mapview.
 * File MapViewUtils.java.
 * Created by Ysee on 02/12/2016 - 15:06.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class MapViewUtils {
    /**
     * Create a new bitmap marker with a specific color.
     * @param color color marker
     * @return a new marker with a specific color
     */
    public static BitmapDescriptor getMarkerIcon(final String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
