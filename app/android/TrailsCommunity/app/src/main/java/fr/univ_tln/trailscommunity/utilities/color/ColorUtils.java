package fr.univ_tln.trailscommunity.utilities.color;

import java.util.Random;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.color.
 * File ColorUtils.java.
 * Created by Ysee on 02/12/2016 - 15:08.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class ColorUtils {
    /**
     * Create a random string hex color.
     * @return string hex color
     */
    public static String randomHex() {
        Random rnd = new Random();
        return String
                .format("#%02X%02X%02X",
                        rnd.nextInt(256),
                        rnd.nextInt(256),
                        rnd.nextInt(256));
    }
}
