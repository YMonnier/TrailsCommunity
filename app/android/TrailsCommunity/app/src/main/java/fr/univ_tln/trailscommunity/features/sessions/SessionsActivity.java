package fr.univ_tln.trailscommunity.features.sessions;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import fr.univ_tln.trailscommunity.R;

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

    @AfterViews
    void init() {
        setTitle(getString(R.string.session_activity_name));
    }

    /**
     * Action click on user profile.
     * Button to view user profile
     */
    @OptionsItem(R.id.sessions_user_menu)
    void userProfileMenuButton() {
        Log.d("SessionsActivity", "Click on userProfileMenuButton");
    }

    /**
     * Action click plus button.
     * Button to view session form.
     */
    @OptionsItem(R.id.sessions_add_session_menu)
    void addSessionMenuButton() {
        Log.d("SessionsActivity", "Click on addSessionMenuButton");
    }
}
