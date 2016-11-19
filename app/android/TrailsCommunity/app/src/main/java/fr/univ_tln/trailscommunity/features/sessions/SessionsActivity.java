package fr.univ_tln.trailscommunity.features.sessions;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.EActivity;

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
public class SessionsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setTitle("Sessions");
    }
}
