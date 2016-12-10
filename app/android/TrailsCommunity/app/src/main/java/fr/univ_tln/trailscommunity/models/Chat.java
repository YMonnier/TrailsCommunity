package fr.univ_tln.trailscommunity.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.models.
 * File Chat.java.
 * Created by Ysee on 10/12/2016 - 15:40.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class Chat extends RealmObject {
    @PrimaryKey
    private int id;

    private String message;

    @SerializedName("user_id")
    private int userId;

    public Chat() {}

    private Chat(Builder builder) {
        this.id = builder.id;
        this.message = builder.message;
        this.userId = builder.userId;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }

    public static class Builder {
        private String message;
        private int userId;
        private int id;

        public Chat build() {
            return new Chat(this);
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }
    }
}
