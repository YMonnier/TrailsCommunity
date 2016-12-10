package fr.univ_tln.trailscommunity.features.session.chatview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import fr.univ_tln.trailscommunity.models.Chat;
import fr.univ_tln.trailscommunity.models.Session;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.features.session.chatview.
 * File ChatListAdapter.java.
 * Created by Ysee on 10/12/2016 - 15:39.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

@EBean
public class ChatListAdapter extends BaseAdapter {

    /**
     * List of message of the current
     * session selected by the user.
     */
    private List<Chat> list;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        list = new ArrayList<>();
    }

    /**
     * Fetch
     * @return
     */
    private List<Chat> loadData(int sessionId) {
        List<Chat> res = new ArrayList<>();

        return res;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
