package fr.univ_tln.trailscommunity.utilities.view;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.view.
 * File ViewUtils.java.
 * Created by Ysee on 26/11/2016 - 15:40.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class ViewUtils {

    /**
     * Close the current keyboard shows.
     * @param context current context.
     * @param focusView view where the keyboard is shown.
     */
    public static void closeKeyboard(final Context context, View focusView){
        if (focusView != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
        }
    }
}
