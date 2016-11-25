package fr.univ_tln.trailscommunity.features.sessions;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import fr.univ_tln.trailscommunity.features.sessions.listview.SessionListAdapter;
import fr.univ_tln.trailscommunity.models.Session;
import fr.univ_tln.trailscommunity.utilities.Snack;
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

    private static final String CLASS_NAME = SessionsActivity.class.getName();

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

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    @AfterViews
    void init() {
        setTitle(getString(R.string.title_sessions_activity));
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
            showProgressBar(AnimationType.FADEIN, 0f, 1f, View.VISIBLE);
            tcRestApi.setHeader("Authorization", Settings.TOKEN_AUTHORIZATION);
            ResponseEntity<JsonObject> sessions = tcRestApi.sessions();
            JsonObject data = sessions.getBody().get("data").getAsJsonObject();

            adapter.addHeader("Active sessions");
            JsonArray activeSessionArray = data.getAsJsonArray("active_sessions");
            Log.d(CLASS_NAME, activeSessionArray.toString());
            Gson gson = new Gson();
            if (activeSessionArray == null)
                throw new AssertionError("activeSessionArray cannot be null");
            if (activeSessionArray != null) {

                for (JsonElement sessionJson : activeSessionArray) {
                    Log.d(CLASS_NAME, sessionJson.toString());
                    adapter.addItem(gson.fromJson(sessionJson, Session.class));
                }
            }

            adapter.addHeader("My sessions");
            JsonArray mySessionArray = data.getAsJsonArray("my_sessions");
            Log.d(CLASS_NAME, mySessionArray.toString());
            if (mySessionArray == null) throw new AssertionError("mySessionArray cannot be null");
            if (mySessionArray != null) {
                for (JsonElement sessionJson : mySessionArray) {
                    adapter.addItem(gson.fromJson(sessionJson, Session.class));
                    Log.d(CLASS_NAME, sessionJson.toString());
                }
            }

            adapter.addHeader("History");
            JsonArray historySessionArray = data.getAsJsonArray("active_sessions");
            Log.d(CLASS_NAME, historySessionArray.toString());
            if (historySessionArray == null)
                throw new AssertionError("historySessionArray cannot be null");
            if (historySessionArray != null) {
                for (JsonElement sessionJson : historySessionArray) {
                    adapter.addItem(gson.fromJson(sessionJson, Session.class));
                    Log.d(CLASS_NAME, sessionJson.toString());
                }
            }
            showProgressBar(AnimationType.FADEOUT, 1f, 0f, View.GONE);
            setAdapter();
        } catch (RestClientException e) {
            showProgressBar(AnimationType.FADEOUT, 1f, 0f, View.GONE);
            Log.d(CLASS_NAME, "error HTTP request: " + e.getLocalizedMessage());
            Snack.showSuccessfulMessage(coordinatorLayout, "Error during the request, please try again.", Snackbar.LENGTH_LONG);
        }
    }

    @UiThread
    void showProgressBar(AnimationType type, float from, float to, int status) {
        if (type == AnimationType.FADEIN) {
            inAnimation = new AlphaAnimation(from, to);
            inAnimation.setDuration(200);
        } else {
            outAnimation = new AlphaAnimation(from, to);
            outAnimation.setDuration(200);
        }
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(status);
    }

    @UiThread
    void setAdapter() {
        sessionList.setAdapter(adapter);
    }

    /**
     * Action click on user profile.
     * Button to view user profile
     */
    @OptionsItem(R.id.sessions_user_menu)
    void userProfileMenuButton() {
        Log.d("SessionsActivity", "Click on userProfileMenuButton");
        startActivity(new Intent(this, ProfileActivity_.class));
        //finish();
    }

    /**
     * Action click plus button.
     * Button to view session form.
     */
    @OptionsItem(R.id.sessions_add_session_menu)
    void addSessionMenuButton() {
        Log.d("SessionsActivity", "Click on addSessionMenuButton");
    }

    /**
     * Action click on item list view.
     *
     * @param session
     */
    @ItemClick
    void sessionListItemClicked(Session session) {
        Toast.makeText(this, session.getActivityName(), LENGTH_SHORT).show();
    }
}
