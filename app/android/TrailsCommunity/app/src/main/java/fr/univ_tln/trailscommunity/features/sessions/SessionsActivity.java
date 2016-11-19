package fr.univ_tln.trailscommunity.features.sessions;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

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

    private ListAdapter adapter;
    private String[] foods = {"A", "B", "C", "D"};
    @AfterViews
    void init() {
        setTitle(getString(R.string.session_activity_name));

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foods);
        //setListAdapter(adapter);
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

    void listItemClicked(String food) {
        Toast.makeText(this, "click: " + food, Toast.LENGTH_SHORT).show();
    }

    void listItemLongClicked(String food) {
        Toast.makeText(this, "long click: " + food, Toast.LENGTH_SHORT).show();
    }

    void listItemSelected(boolean somethingSelected, String food) {
        if (somethingSelected) {
            Toast.makeText(this, "selected: " + food, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "nothing selected", Toast.LENGTH_SHORT).show();
        }
    }
}
