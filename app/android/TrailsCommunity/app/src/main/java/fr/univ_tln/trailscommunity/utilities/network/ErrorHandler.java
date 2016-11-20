package fr.univ_tln.trailscommunity.utilities.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.api.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.network.
 * File ErrorHandler.java.
 * Created by Ysee on 20/11/2016 - 17:39.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

@EBean
public class ErrorHandler implements RestErrorHandler {
    @RootContext
    Context context;

    @Override
    public void onRestClientExceptionThrown(NestedRuntimeException e) {
        e.printStackTrace();
        Log.d("REST", e.toString());
        showError();
    }

    @UiThread
    void showError(){
        Toast.makeText(context, "Rest Error...", Toast.LENGTH_SHORT).show();
    }
}
