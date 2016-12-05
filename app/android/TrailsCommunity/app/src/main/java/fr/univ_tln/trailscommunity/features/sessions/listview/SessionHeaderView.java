package fr.univ_tln.trailscommunity.features.sessions.listview;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import fr.univ_tln.trailscommunity.R;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.features.sessions.
 * File SessionItemView.java.
 * Created by Ysee on 19/11/2016 - 16:58.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */
@EViewGroup(R.layout.sessions_sessions_list_header)
public class SessionHeaderView extends LinearLayout {

    /**
     * Constant header name for active session.
     */
    public static final String ACTIVE_SESSION_HEADER = "Active sessions";

    /**
     * Constant header name for session created by current user.
     */
    public static final String MY_SESSION_HEADER = "My sessions";

    /**
     * Constant header name for history session.
     */
    public static final String HISTORY_HEADER = "History";

    @ViewById
    TextView titleTextView;

    public SessionHeaderView(Context context) {
        super(context);
    }

    public void bind(String title) {
        titleTextView.setText(title);
    }
}
