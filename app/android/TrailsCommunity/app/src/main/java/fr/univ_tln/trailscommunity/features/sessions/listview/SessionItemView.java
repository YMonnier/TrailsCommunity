package fr.univ_tln.trailscommunity.features.sessions.listview;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.models.Session;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.features.sessions.
 * File SessionItemView.java.
 * Created by Ysee on 19/11/2016 - 16:58.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */
@EViewGroup(R.layout.sessions_sessions_list_item)
public class SessionItemView extends LinearLayout {

    @ViewById
    TextView titleTextView;

    @ViewById
    ImageView imageView;

    public SessionItemView(Context context) {
        super(context);
    }

    public void bind(Session session) {
        if (session.isLock())
            imageView.setVisibility(VISIBLE);
        else
            imageView.setVisibility(INVISIBLE);

        titleTextView.setText(String.format("Session %s", session.getActivityName()));
    }
}
