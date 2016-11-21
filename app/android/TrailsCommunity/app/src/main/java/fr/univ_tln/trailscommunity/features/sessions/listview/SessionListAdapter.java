package fr.univ_tln.trailscommunity.features.sessions.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univ_tln.trailscommunity.models.Session_old;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.features.sessions.
 * File SessionListAdapter.java.
 * Created by Ysee on 19/11/2016 - 16:47.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

@EBean
public class SessionListAdapter extends BaseAdapter {

    /**
     * Item type identifier
     */
    private static final int ITEM_TYPE = 0;

    /**
     * Header type identifier
     */
    private static final int HEADER_TYPE = 1;

    private List<Session_old> sessions;
    private Map<Integer, String> headers;

    @RootContext
    Context context;

    // Instead of initAdapter
    // void fetch(session_id) --> HTTP Request
    // +(optional) add loader view?

    @AfterInject
    void initAdapter() {
        sessions = new ArrayList<>();
        headers = new HashMap<>();

        addHeader("Active sessions");
        addItem(new Session_old.Builder().setActivity(1).build());
        addItem(new Session_old.Builder().setActivity(2).build());
        addHeader("My sessions");
        addItem(new Session_old.Builder().setActivity(1).build());
        addItem(new Session_old.Builder().setActivity(2).build());
        addHeader("History");
        addItem(new Session_old.Builder().setActivity(1).build());
        addItem(new Session_old.Builder().setActivity(2).build());
        addItem(new Session_old.Builder().setActivity(1).build());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int type = getItemViewType(i);
        if (view == null) {
            if (type == ITEM_TYPE) {
                SessionItemView item = SessionItemView_.build(context);
                item.bind(getItem(i));
                return item;
            } else {
                SessionHeaderView header = SessionHeaderView_.build(context);
                header.bind(headers.get(i));
                return header;
            }
        } else {
            if(view instanceof  SessionItemView)
                return (SessionItemView) view;
            else
                return (SessionHeaderView) view;
        }
    }

    public void addItem(final Session_old session) {
        assert sessions != null;
        sessions.add(session);
    }

    public void addHeader(final String header) {
        sessions.add(new Session_old.Builder().build());
        headers.put(sessions.size() - 1, header);
    }

    @Override
    public boolean isEnabled(int position) {
        int type = getItemViewType(position);
        return type == ITEM_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return headers.containsKey(position)
                ? HEADER_TYPE : ITEM_TYPE;
    }

    @Override
    public int getCount() {
        return this.sessions.size();
    }

    @Override
    public Session_old getItem(int i) {
        return this.sessions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
