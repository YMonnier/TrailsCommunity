package fr.univ_tln.trailscommunity.features.sessions;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.Settings;
import fr.univ_tln.trailscommunity.features.root.ProfileActivity_;
import fr.univ_tln.trailscommunity.features.session.SessionFormActivity_;
import fr.univ_tln.trailscommunity.features.sessions.listview.SessionListAdapter;
import fr.univ_tln.trailscommunity.models.Session;
import fr.univ_tln.trailscommunity.utilities.Snack;
import fr.univ_tln.trailscommunity.utilities.loader.LoaderDialog;
import fr.univ_tln.trailscommunity.utilities.network.TCRestApi;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.features.session.
 * File SessionsActivity.java.
 * Created by Ysee on 19/11/2016 - 13:48.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

@EActivity(R.layout.sessions_sessions_activity)
@OptionsMenu(R.menu.sessions_sessions_menu)
public class SessionsActivity extends AppCompatActivity {

    /**
     * Tag used for Logger.
     */
    private static final String TAG = SessionsActivity.class.getSimpleName();

    enum AnimationType {
        FADEIN, FADEOUT
    }

    @ViewById
    ListView sessionList;

    @Bean
    SessionListAdapter adapter;

    @ViewById
    FrameLayout progressBarHolder;

    @ViewById
    CoordinatorLayout coordinatorLayout;

    @RestService
    TCRestApi tcRestApi;

    /**
     * Progress Dialog
     */
    private LoaderDialog progressView;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    @AfterViews
    void init() {
        setTitle(getString(R.string.title_sessions_activity));
        progressView = new LoaderDialog(this, getString(R.string.fetching_session));
        progressView.show();
        loadData();
        //sessionList.setAdapter(adapter);
    }

    /**
     * Method which allows you to
     * fetch all sessions data from API.
     * Moreover, it checks if sessions create by
     * current user are present into database and use it.
     */
    @Background
    void loadData() {
        try {
            if (adapter == null)
                throw new AssertionError("The adapter should not be null");

            if (adapter != null) {

                tcRestApi.setHeader("Authorization", Settings.TOKEN_AUTHORIZATION);
                ResponseEntity<JsonObject> responseSessions = tcRestApi.sessions();

                if (responseSessions == null)
                    throw new AssertionError("response sessions should not be null");

                if (responseSessions != null) {
                    if (responseSessions.getStatusCode().is2xxSuccessful()) {
                        JsonObject data = responseSessions.getBody().get("data").getAsJsonObject();
                        Gson gson = new Gson();
                        String sessionKey = "session";

                        adapter.addHeader("Active sessions");
                        JsonArray activeSessionArray = data.getAsJsonArray("active_sessions");
                        Log.d(TAG, activeSessionArray.toString());
                        if (activeSessionArray == null)
                            throw new AssertionError("activeSessionArray cannot be null");
                        if (activeSessionArray != null) {
                            parseSessionItemJsonObject(activeSessionArray);
                        }

                        adapter.addHeader("My sessions");
                        JsonArray mySessionArray = data.getAsJsonArray("my_sessions");
                        Log.d(TAG, mySessionArray.toString());
                        if (mySessionArray == null)
                            throw new AssertionError("mySessionArray cannot be null");
                        if (mySessionArray != null) {
                            parseSessionItemJsonObject(mySessionArray);
                        }

                        adapter.addHeader("History");
                        JsonArray historySessionArray = data.getAsJsonArray("active_sessions");
                        Log.d(TAG, historySessionArray.toString());
                        if (historySessionArray == null)
                            throw new AssertionError("historySessionArray cannot be null");
                        if (historySessionArray != null) {
                            parseSessionItemJsonObject(historySessionArray);
                        }
                        setAdapter();
                    } else
                        Snack.showSuccessfulMessage(coordinatorLayout, getString(R.string.error_request_4xx_5xx_status), Snackbar.LENGTH_LONG);
                } else
                    Snack.showSuccessfulMessage(coordinatorLayout, getString(R.string.error_request_4xx_5xx_status), Snackbar.LENGTH_LONG);
                progressView.dismiss();
            }
        } catch (RestClientException e) {
            Log.d(TAG, "error HTTP request: " + e.getLocalizedMessage());
            progressView.dismiss();
            Snack.showSuccessfulMessage(coordinatorLayout, getString(R.string.error_request_4xx_5xx_status), Snackbar.LENGTH_LONG);
        }
    }

    /**
     * Parse an array of json object from the list of session.
     */
    private void parseSessionItemJsonObject(final JsonArray jsonArray) {
        if (jsonArray == null)
            throw new AssertionError("The jsonArray should not be null");

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        if (jsonArray != null && gson != null) {
            String sessionKey = "session";
            for (JsonElement sessionJson : jsonArray) {
                JsonObject jsonObject = sessionJson.getAsJsonObject();
                if (jsonObject != null) {
                    JsonObject sessionJsonObject = jsonObject.getAsJsonObject(sessionKey);
                    if (sessionJsonObject != null)
                        adapter.addItem(gson.fromJson(sessionJsonObject, Session.class));
                }
            }
        }
    }

    @UiThread
    void setAdapter() {
        sessionList.setAdapter(adapter);
    }

    /**
     * Action click plus button.
     * Button to view session form.
     */
    @OptionsItem(R.id.sessions_add_session_menu)
    void addSessionMenuButton() {
        Log.d("SessionsActivity", "Click on addSessionMenuButton");
        startActivity(new Intent(this, SessionFormActivity_.class));
    }

    @OptionsItem(R.id.user_profile)
    void addUserProfileMenuButton() {
        Log.d("SessionsActivity", "Click on addUserProfileMenuButton");
        startActivity(new Intent(this, ProfileActivity_.class));
    }

    @OptionsItem(R.id.logout)
    void addLogoutMenuButton() {
        Log.d("SessionActivity", "Click on addLogoutMenuButton");

        //startActivity(new Intent(this, LoginActivity_.class));
        //finish();
    }

    /**
     * Action click on item list view.
     *
     * @param session
     */
    @ItemClick
    void sessionListItemClicked(Session session) {
        Log.e(TAG, session.toString());
        if (session.isLock()) {
            showPasswordDialog();
        }

        Toast.makeText(this, session.getActivityName(), LENGTH_SHORT).show();
    }

    /**
     * Show password dialog
     */
    private void showPasswordDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_signin, null))
                // Add action buttons
                .setPositiveButton(R.string.action_sign_in, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton(R.string.cancel_action, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
