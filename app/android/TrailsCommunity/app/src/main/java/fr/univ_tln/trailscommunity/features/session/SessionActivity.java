package fr.univ_tln.trailscommunity.features.session;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.features.session.navigation.MapNavigation;
import fr.univ_tln.trailscommunity.models.Session;

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

    /**
     * Creating facade for
     * the map navigation manipulation.
     */
    @Bean(MapNavigation.class)
    MapNavigation mapNavigation;

    @Extra(SESSION_ID_EXTRA)
    int sessionId;

    protected Session session;

    private List<String> chatMessageList;

    @AfterViews
    void init() {
        Log.d(TAG, "init AfterViews....");
        session = new Session.Builder()
                .setActivity(Session.TypeActivity.HIKING.ordinal())
                .build();
        mapNavigation.init(session);

        this.chatMessageList = new ArrayList<>();
        chatMessageList.add("Robert : Test loooooonnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnng message.");

        // Set the adapter for the list view
        chatListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, chatMessageList));
    }

    @Click(R.id.sendMessage)
    void sendMessage(){
        String message = chatField.getText().toString();
        String nickname = "Nickname";

        if(!TextUtils.isEmpty(message)){
            chatMessageList.add(nickname + " : " + message);
            chatListView.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, chatMessageList));
            chatField.setText(null);
        }
        else{
            chatField.setError(getString(R.string.error_field_required));
        }
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
    void clickOnFloatingButton(View view) {
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
                "Time: " + time + " minutes");
        textViewTop.setTextColor(Color.WHITE);

        layout.addView(snackView, 0);
        snackbar.show();
    }

    @Override
    public void finish() {
        super.finish();
        if (mapNavigation != null) {
            mapNavigation.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapNavigation != null) {
            mapNavigation.stop();
        }
    }
}
