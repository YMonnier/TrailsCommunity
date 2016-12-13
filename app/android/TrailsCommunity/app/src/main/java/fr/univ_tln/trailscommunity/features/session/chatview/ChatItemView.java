package fr.univ_tln.trailscommunity.features.session.chatview;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.models.Chat;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.features.session.chatview.
 * File ChatItemView.java.
 * Created by Ysee on 13/12/2016 - 15:29.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

@EViewGroup(R.layout.sessions_sessions_chat_list_item)
public class ChatItemView extends LinearLayout {
    @ViewById
    TextView titleTextView;

    public ChatItemView(Context context) {
        super(context);
    }

    public void bind(Chat chat) {
        if (titleTextView != null)
            titleTextView.setText(String.format("%s: %s", "John", chat.getMessage()));
    }
}
