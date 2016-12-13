package fr.univ_tln.trailscommunity.features.session.chatview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.Settings;
import fr.univ_tln.trailscommunity.features.sessions.listview.SessionHeaderView;
import fr.univ_tln.trailscommunity.features.sessions.listview.SessionHeaderView_;
import fr.univ_tln.trailscommunity.features.sessions.listview.SessionItemView;
import fr.univ_tln.trailscommunity.features.sessions.listview.SessionItemView_;
import fr.univ_tln.trailscommunity.models.Chat;
import fr.univ_tln.trailscommunity.models.Session;
import fr.univ_tln.trailscommunity.utilities.json.GsonSingleton;
import fr.univ_tln.trailscommunity.utilities.network.NetworkUtils;
import fr.univ_tln.trailscommunity.utilities.network.ResApiKey;
import fr.univ_tln.trailscommunity.utilities.network.TCRestApi;
import io.realm.Realm;

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
     * Tag used for Logger.
     */
    private static final String TAG = ChatListAdapter.class.getName();

    /**
     * List of message of the current
     * session selected by the user.
     */
    private List<Chat> list;

    private LayoutInflater inflater;

    @RootContext
    Context context;

    @RestService
    TCRestApi tcRestApi;

    @AfterInject
    void initAdapter() {
        list = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Initialize the chat list
     *
     * @param sessionId current session id
     */
    public void init(int sessionId) {
        loadData(sessionId);
    }

    /**
     * Fetch all message.
     * If there is no internet connection, we get data from local database,
     * otherwise, from the API
     *
     * @return list of message
     */
    @Background
    void loadData(int sessionId) {
        tcRestApi.setHeader(Settings.AUTHORIZATION_HEADER_NAME, Settings.TOKEN_AUTHORIZATION);
        if (!NetworkUtils.isNetworkConnected(context)) {
            /*Realm realm = Realm.getDefaultInstance();
            if (realm != null) {
                Session session = realm.where(Session.class).equalTo("id", sessionId).findFirst();
                if (session != null) {
                    if (session.getChats() != null) {
                        res = session.getChats();
                    }
                }
            }*/
        } else {
            ResponseEntity<JsonObject> response = tcRestApi.messages(sessionId);
            if (response != null) {
                if (response.getStatusCode().is2xxSuccessful()) {
                    JsonObject jsonObject = response.getBody();
                    JsonArray data = jsonObject.getAsJsonArray(ResApiKey.DATA);
                    if (data != null) {
                        for (JsonElement jsonElement : data) {
                            Chat c = GsonSingleton.getInstance().fromJson(jsonElement, Chat.class);
                            if (c != null)
                                list.add(c);
                        }
                    }
                }
            }
        }
    }

    /**
     * Method which allows to add a new message to the chat view.
     * Moreover, the message will be broadcasted via
     * http request then via push notification through the server.
     *
     * @param chat message
     */
    public void addMessage(Chat chat) {
        Log.d(TAG, "add message::: " + chat);
        if (chat == null)
            throw new AssertionError("The chat message should not be null");
        if (chat != null && list != null) {
            if (chat.getMessage() != null && chat.getUser() != null) {
                list.add(chat);
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Chat chat = getItem(i);
        ChatItemView item = ChatItemView_.build(context);
        item.bind(chat);
        return item;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Chat getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
