package fr.univ_tln.trailscommunity.utilities.loader;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.UiThread;

import fr.univ_tln.trailscommunity.R;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.loader.
 * File LoaderDialog.java.
 * Created by Ysee on 04/12/2016 - 12:06.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class LoaderDialog {

    private ProgressDialog progressView;

    /**
     * Create a new `ProgressDialog`with a specific theme and message.
     *
     * @param context target view
     * @param message message to display
     */
    public LoaderDialog(Context context, String message) {
        progressView = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);

        if (progressView == null)
            throw new AssertionError("progress view should not be null");

        if (progressView != null) {
            progressView.setIndeterminate(true);
            progressView.setMessage(message);
        }
    }

    /**
     * Show the current progress view on background ui thread
     */
    @UiThread
    public void show() {
        if (progressView != null)
            progressView.show();
    }

    /**
     * hide the current progress view
     */
    @UiThread
    public void dismiss() {
        if (progressView != null)
            progressView.dismiss();
    }
}
