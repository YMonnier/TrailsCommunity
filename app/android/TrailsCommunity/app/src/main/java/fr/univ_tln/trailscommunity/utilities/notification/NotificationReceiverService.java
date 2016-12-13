package fr.univ_tln.trailscommunity.utilities.notification;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
     * Intent identifier for location broadcast from notification.
     */
    public static final String COORDINATE_SHARING_RECEIVER = "COORDINATE_SHARING_RECEIVER";
    public static final String EXTRA_COORDINATE_SHARING_RECEIVER = "EXTRA_COORDINATE_SHARING_RECEIVER";

    /**
     * Intent identifier for waypoint broadcast from notification.
     */
    public static final String WAYPOINT_SHARING_RECEIVER = "WAYPOINT_SHARING_RECEIVER";
    public static final String EXTRA_WAYPOINT_SHARING_RECEIVER = "EXTRA_WAYPOINT_SHARING_RECEIVER";

    /**
     * Intent identifier for waypoint broadcast from notification.
     */
    public static final String CHAT_SHARING_RECEIVER = "CHAT_SHARING_RECEIVER";
    public static final String EXTRA_CHAT_SHARING_RECEIVER = "EXTRA_CHAT_SHARING_RECEIVER";

    /**
     * Different type of message thah application
     * can receive from a push notification(Firebase Cloud Messaging, previous Google Cloud Messaging).
     */
    enum MessageType {
        WAYPOINT("waypoint"),
        COORDINATE("coordinate"),
        CHAT("chat_message");

        String type;

        MessageType(String type) {
            this.type = type;
        }
    }

    /**
     * Receive a notification which has been
     * pushed by the server to broadcast a session information/message.
     * The message can be a coordinate, user name, updating information.
     *
     * @param remoteMessage message pushed
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());

        if (remoteMessage.getData().containsKey("data")) {
            Gson gson = new Gson();
            JsonObject data = gson.fromJson(remoteMessage.getData().get("data"), JsonObject.class);
            String type = data.get("type").getAsString();
            if (type == null)
                throw new AssertionError("type key from notification should not be null");

            if (type != null) {

                JsonObject jsonObject = data.get("content").getAsJsonObject();
                if (jsonObject == null)
                    throw new AssertionError("json content key from notification should not be null");
                if (jsonObject != null) {

                    String json = gson.toJson(jsonObject);
                    if (json == null)
                        throw new AssertionError("json message from notification should not be null");
                    if (json != null) {
                        if (type.equals(MessageType.COORDINATE.type)) {
                            Intent intent = new Intent(COORDINATE_SHARING_RECEIVER);
                            intent.putExtra(EXTRA_COORDINATE_SHARING_RECEIVER, json);
                            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                        } else if (type.equals(MessageType.WAYPOINT.type)) {
                            Intent intent = new Intent(WAYPOINT_SHARING_RECEIVER);
                            intent.putExtra(EXTRA_WAYPOINT_SHARING_RECEIVER, json);
                            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                        } else if (type.equals(MessageType.CHAT.type)) {
                            Intent intent = new Intent(CHAT_SHARING_RECEIVER);
                            intent.putExtra(EXTRA_CHAT_SHARING_RECEIVER, json);
                            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                        }
                    }
                }
            }
        }

    }
}
