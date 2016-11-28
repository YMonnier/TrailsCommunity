package fr.univ_tln.trailscommunity.features.session;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.features.session.navigation.MapNavigation;
import fr.univ_tln.trailscommunity.models.Session;

@EActivity(R.layout.session_session_activity)
public class SessionActivity extends AppCompatActivity {

    private static final String TAG = SessionActivity.class.getName();

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @ViewById(R.id.left_drawer)
    RelativeLayout drawerLinear;

    @ViewById(R.id.chat_list_view)
    ListView chatListView;

    @ViewById(R.id.fab)
    FloatingActionButton floatingActionButton;

    /**
     * Creating facade for
     * the map navigation manipulation.
     */
    @Bean(MapNavigation.class)
    MapNavigation mapNavigation;

    protected Session session;

    private List<String> chatMessageList;

    @AfterViews
    void init(){
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
                if(!drawerLayout.isDrawerOpen(Gravity.LEFT)){
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                else{
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
     * @param view
     */
    @Click(R.id.fab)
    void clickOnFloatingButton(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
