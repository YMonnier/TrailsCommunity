package fr.univ_tln.trailscommunity.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.models.
 * File User.java.
 * Created by Ysee on 21/11/2016 - 15:36.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class User extends RealmObject {
    @PrimaryKey
    private int id;
    private String nickname;
    private String email;
    private String phoneNumber;
    private RealmList<Session> sessions;

    public User(Builder builder) {
        this.id = builder.id;
        this.nickname = builder.nickname;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public RealmList<Session> getSessions() {
        return sessions;
    }

    public static class Builder {
        private int id;
        private String nickname;
        private String email;
        private String phoneNumber;
        private RealmList<Session> sessions;

        public User build(){
            return new User(this);
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setNickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setSessions(RealmList<Session> sessions) {
            this.sessions = sessions;
            return this;
        }
    }
}
