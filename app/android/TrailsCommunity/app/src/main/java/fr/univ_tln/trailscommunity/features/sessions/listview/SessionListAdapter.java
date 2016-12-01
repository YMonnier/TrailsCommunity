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

import fr.univ_tln.trailscommunity.models.Session;

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

    /**
     * List of session
     */
    private List<Session> sessions;

    private Map<Integer, String> headers;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        sessions = new ArrayList<>();
        headers = new HashMap<>();
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
            if (view instanceof SessionItemView)
                return (SessionItemView) view;
            else
                return (SessionHeaderView) view;
        }
    }

    public void addItem(final Session session) {
        if (session == null)
            throw new AssertionError("Session should not be null");
        if (session != null && sessions != null)
            sessions.add(session);
    }

    public void addHeader(final String header) {
        if (header == null)
            throw new AssertionError("Header string should not be null");
        if (sessions != null && headers != null) {
            sessions.add(new Session.Builder().build());
            headers.put(sessions.size() - 1, header);
        }
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
    public Session getItem(int i) {
        return this.sessions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
