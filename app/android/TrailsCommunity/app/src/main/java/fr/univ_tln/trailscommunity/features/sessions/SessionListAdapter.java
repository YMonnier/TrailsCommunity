package fr.univ_tln.trailscommunity.features.sessions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

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

    List<Session> sessions;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        Session s = new Session.Builder().setArrivalPlace("test").build();
        sessions = new ArrayList<>();
        sessions.add(s);

        //sessions = { }
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
