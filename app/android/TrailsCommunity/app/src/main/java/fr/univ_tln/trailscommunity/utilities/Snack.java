package fr.univ_tln.trailscommunity.utilities;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.
 * File Snack.java.
 * Created by Ysee on 21/11/2016 - 16:36.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */
public class Snack {

    /**
     * CReate a snackbar view to show a succesfull message.
     * @param view target view
     * @param message message to display
     * @param duration duration constant
     */
    public static void showSuccessfulMessage(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }

    /**
     * CReate a snackbar view to show a failure message.
     * @param view target view
     * @param message message to display
     * @param duration duration constant
     */
    public static void showFailureMessage(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }
}
