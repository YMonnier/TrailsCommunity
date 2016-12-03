package fr.univ_tln.trailscommunity.models;

import com.google.gson.annotations.SerializedName;

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
    @SerializedName("phone_number")
    private String phoneNumber;
    //private RealmList<Session> sessions;
    private boolean active = false;

    public User(){}

    public User(Builder builder) {
        this.id = builder.id;
        this.nickname = builder.nickname;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        //this.sessions = new RealmList<>();
        this.active = true;
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

/*    public RealmList<Session> getSessions() {
        return sessions;
    }
    */

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static class Builder {
        private int id;
        private String nickname;
        private String email;
        private String phoneNumber;
        private boolean active = false;
      //  private RealmList<Session> sessions;


        public User build(){
            return new User(this);
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setActive(boolean active) {
            this.active = active;
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
/*
        public Builder setSessions(RealmList<Session> sessions) {
            this.sessions = sessions;
            return this;
        }*/
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", active=" + active +
                '}';
    }
}
