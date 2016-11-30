package fr.univ_tln.trailscommunity.utilities.notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.notification.
 * File NotificationTokenService.java.
 * Created by Ysee on 30/11/2016 - 16:21.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class NotificationTokenService extends FirebaseInstanceIdService {
    private static final String TAG = NotificationTokenService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FCM Token: " + token);
    }
}
