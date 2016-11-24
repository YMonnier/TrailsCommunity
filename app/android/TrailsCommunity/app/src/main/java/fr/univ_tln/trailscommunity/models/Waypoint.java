package fr.univ_tln.trailscommunity.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.models.
 * File Waypoint.java.
 * Created by Ysee on 21/11/2016 - 15:42.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class Waypoint extends RealmObject {
    private double latitude;
    private double longitude;
    private boolean sent;

    private Waypoint(Builder builder) {
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public static class Builder {
        private double latitude;
        private double longitude;
        private boolean sent = false;

        public Waypoint build() {
            return new Waypoint(this);
        }

        public Builder setLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder setLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder setSent(boolean sent) {
            this.sent = sent;
            return this;
        }
    }

}
