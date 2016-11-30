package fr.univ_tln.trailscommunity.utilities.notification;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.notification.
 * File NotificationReceiverService.java.
 * Created by Ysee on 30/11/2016 - 16:23.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class NotificationReceiverService extends FirebaseMessagingService {
    private static final String TAG = NotificationReceiverService.class.getSimpleName();

    /**
     * Receive a notification which has been
     * pushed by the server to broadcast a session information/message.
     * The message can be a coordinate, user name, updating information.
     * @param remoteMessage message pushed
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());
    }
}
