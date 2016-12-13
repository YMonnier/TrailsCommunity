package fr.univ_tln.trailscommunity.features.session;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.Settings;
import fr.univ_tln.trailscommunity.features.session.chatview.ChatListAdapter;
import fr.univ_tln.trailscommunity.features.session.navigation.MapNavigation;
import fr.univ_tln.trailscommunity.models.Chat;
import fr.univ_tln.trailscommunity.models.Session;
import fr.univ_tln.trailscommunity.models.User;
import fr.univ_tln.trailscommunity.utilities.Snack;
import fr.univ_tln.trailscommunity.utilities.json.GsonSingleton;
import fr.univ_tln.trailscommunity.utilities.network.TCRestApi;
import fr.univ_tln.trailscommunity.utilities.notification.NotificationReceiverService;
import io.realm.Realm;

@EActivity(R.layout.session_session_activity)
public class SessionActivity extends AppCompatActivity {

    /**
     * Tag used for Logger.
     */
    private static final String TAG = SessionActivity.class.getName();

    /**
     * Extra identifier used to send
     * session id value through `SessionsActivity`.
     */
    public static final String SESSION_ID_EXTRA = "SESSION_ID_EXTRA";

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @ViewById(R.id.left_drawer)
    RelativeLayout drawerLinear;

    @ViewById(R.id.chat_list_view)
    ListView chatListView;

    @ViewById(R.id.fab)
    FloatingActionButton floatingActionButton;

    @ViewById(R.id.chatField)
    EditText chatField;

    @ViewById(R.id.sendMessage)
    Button sendMessage;

    @ViewById(R.id.sessionActivity)
    CoordinatorLayout coordinatorLayout;

    /**
     * Creating facade for
     * the map navigation manipulation.
     */
    @Bean(MapNavigation.class)
    MapNavigation mapNavigation;

    /**
     * List view adapter used
     * to manage the chat.
     */
    @Bean
    ChatListAdapter chatListAdapter;

    @RestService
    TCRestApi tcRestApi;

    @Extra(SESSION_ID_EXTRA)
    int sessionId;

    protected Session session;

    @AfterViews
    void init() {
        Log.d(TAG, "init afterViews.... sessionId: " + sessionId);
        Realm realm = Realm.getDefaultInstance();
        this.session = realm.where(Session.class).equalTo("id", sessionId).findFirst();

        if (session != null) {
            mapNavigation.init(session);
        }

        chatListAdapter.init(sessionId);
        chatListView.setAdapter(chatListAdapter);
        // Add observer broadcast messaging for chatting.
        LocalBroadcastManager.getInstance(this).registerReceiver(chatMessageSharingReceiver,
                new IntentFilter(NotificationReceiverService.CHAT_SHARING_RECEIVER));
    }

    /**
     * Action to send a message to the current session
     */
    @Click(R.id.sendMessage)
    void sendMessageAction() {
        Log.d(TAG, "Send message action...");
        String message = chatField.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            Chat chat = new Chat.Builder()
                    .setUserId(Settings.userId)
                    .setMessage(message)
                    .setUser(new User.Builder().setId(Settings.userId)
                            .build())
                    .build();
            sendMessage(chat);
            chatField.setText(null);
        } else {
            chatField.setError(getString(R.string.error_field_required));
        }
    }

    /**
     * Add a message to the chat list adapter
     *
     * @param chatMessage chat message
     */
    @Background
    void sendMessage(final Chat chatMessage) {
        Log.d(TAG, "sendMessage HTTP....");
        try {
            tcRestApi.setHeader(Settings.AUTHORIZATION_HEADER_NAME, Settings.TOKEN_AUTHORIZATION);
            tcRestApi.sendMessage(sessionId, chatMessage);
            updateUi(chatMessage);
            /*Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            Session s = realm.where(Session.class).equalTo("id", sessionId).findFirst();
            if(s != null){
                s.getChats().add(chatMessage);
                realm.copyToRealmOrUpdate(chatMessage);
            }
            realm.commitTransaction();
            */
        } catch (RestClientException e) {
            Log.d(TAG, "error HTTP request: " + e.getLocalizedMessage());
            Snack.showFailureMessage(coordinatorLayout, getString(R.string.error_request_4xx_5xx_status), Snackbar.LENGTH_LONG);
        }
    }

    @UiThread
    void updateUi(final Chat chatMessage) {
        Log.d(TAG, "updateUi Add message to listView");
        chatListAdapter.addMessage(chatMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.session_session_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.session_chat_menu:
                if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Floating action button which allows
     * to display the current user statistics.
     *
     * @param view
     */
    @Click(R.id.fab)
    void clickOnFloatingButton(final View view) {
        double speed = 14.5;
        double distance = 14340;
        double time = 140;


        Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);

        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        View snackView = this.getLayoutInflater().inflate(R.layout.session_session_statistics_snackbar, null);

        TextView textViewTop = (TextView) snackView.findViewById(R.id.statistics_content);
        textViewTop.setText("Statistics\n\n" +
                "Average speed: " + speed + " km/h\n" +
                "Distance: " + distance + " meters\n" +
                "Time: " + time + " minutes\n");
        textViewTop.setTextColor(Color.WHITE);

        layout.addView(snackView, 0);
        snackbar.show();
    }

    /**
     * Handler for receive the waypoint intent from notification(NotificationReceiverService). This will be
     * called whenever an Intent with an action named `LocationService.ASK_LOCATION_SETTINGS`
     * is broadcasted.
     */
    private BroadcastReceiver chatMessageSharingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String json = intent.getStringExtra(NotificationReceiverService.EXTRA_CHAT_SHARING_RECEIVER);
            Chat chat = GsonSingleton.getInstance().fromJson(json, Chat.class);
            Log.d(TAG, "Got chat message from notification: " + chat);
            updateUi(chat);
        }
    };

    @Override
    public void finish() {
        super.finish();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(chatMessageSharingReceiver);
        if (mapNavigation != null) {
            mapNavigation.stop();
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(chatMessageSharingReceiver);
        super.onDestroy();
        if (mapNavigation != null) {
            mapNavigation.stop();
        }
    }
}
