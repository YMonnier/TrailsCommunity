package fr.univ_tln.trailscommunity.models;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.models.
 * File Coordinate.java.
 * Created by Ysee on 21/11/2016 - 15:40.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class Coordinate extends RealmObject {
    private double latitude;
    private double longitude;
    private boolean sent = false;

    public Coordinate(){}

    public Coordinate(Builder builder) {
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

        public Coordinate build() {
            return new Coordinate(this);
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

    @Override
    public String toString() {
        return "Coordinate{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", sent=" + sent +
                '}';
    }
}
