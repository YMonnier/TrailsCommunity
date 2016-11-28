package fr.univ_tln.trailscommunity.features.session.navigation.location;

import fr.univ_tln.trailscommunity.models.Session;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.features.session.navigation.
 * File LocationSetting.java.
 * Created by Ysee on 28/11/2016 - 17:15.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class LocationSetting {
    public static final String EXTRA_DISTANCE = "EXTRA_DISTANCE";
    public static final String EXTRA_TIME_MILLIS = "EXTRA_TIME_MILLIS";

    private int distance;
    private int time;

    private LocationSetting(Builder builder) {
        this.distance = builder.distance;
        this.time = builder.time;
    }


    public static LocationSetting fromActivity(Session.TypeActivity typeActivity) {
        LocationSetting res = null;
        switch (typeActivity) {
            case OFFROAD4x4:
                res = new LocationSetting.Builder()
                    .setDistance(50)
                    .setTime(1000 * 60 * 1).build();
                break;
            case BICYCLE:
                res = new LocationSetting.Builder()
                        .setDistance(20)
                        .setTime(1000 * 60 * 1).build();
                break;
            case BOAT:
                res = new LocationSetting.Builder()
                        .setDistance(100)
                        .setTime(1000 * 60 * 1).build();
                break;
            case HIKING:
                res = new LocationSetting.Builder()
                        .setDistance(10)
                        .setTime(1000).build();
                break;
            case PAINTBALL:
                res = new LocationSetting.Builder()
                        .setDistance(10)
                        .setTime(1000 * 60 * 1).build();
                break;
            case RUNNING:
                res = new LocationSetting.Builder()
                        .setDistance(15)
                        .setTime(1000 * 60 * 1).build();
                break;
        }
        return res;
    }

    public int getDistance() {
        return distance;
    }

    public int getTime() {
        return time;
    }

    public static class Builder {
        private int distance;
        private int time;

        public LocationSetting build() {
            return new LocationSetting(this);
        }

        public Builder setDistance(int distance) {
            this.distance = distance;
            return this;
        }

        public Builder setTime(int time) {
            this.time = time;
            return this;
        }
    }
}
