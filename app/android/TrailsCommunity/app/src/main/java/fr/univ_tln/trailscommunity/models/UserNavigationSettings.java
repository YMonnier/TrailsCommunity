package fr.univ_tln.trailscommunity.models;

import android.graphics.Color;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.models.
 * File UserNavigationSettings.java.
 * Created by Ysee on 02/12/2016 - 13:08.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class UserNavigationSettings {
    private int id;
    private String color;
    private Marker marker;
    private Polyline polyline;

    private UserNavigationSettings(Builder builder) {
        this.id = builder.id;
        this.color = builder.color;
        this.marker = builder.marker;
        this.polyline = builder.polyline;
    }

    public int getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public Marker getMarker() {
        return marker;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public static class Builder {
        private int id;
        private String color;
        private Marker marker;
        private Polyline polyline;

        public UserNavigationSettings build() {
            return new UserNavigationSettings(this);
        }

        public Builder setPolyline(Polyline polyline) {
            this.polyline = polyline;
            return this;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setColor(String color) {
            this.color = color;
            return this;
        }

        public Builder setMarker(Marker marker) {
            this.marker = marker;
            return this;
        }
    }
}
