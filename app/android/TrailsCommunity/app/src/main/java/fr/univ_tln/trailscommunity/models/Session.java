package fr.univ_tln.trailscommunity.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.models.
 * File Session.java.
 * Created by Ysee on 21/11/2016 - 15:39.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class Session extends RealmObject {
    @PrimaryKey
    private int id;
    private int activity;
    private boolean lock;
    private boolean close;
    private String departurePlace;
    private String arrivalPlace;
    private Date startDate;
    private RealmList<Coordinate> coordinates;
    private RealmList<Waypoint> waypoints;

    public Session(){}

    public Session(Builder builder) {
        this.id = builder.id;
        this.activity = builder.activity;
        this.lock = builder.lock;
        this.close = builder.close;
        this.departurePlace = builder.departurePlace;
        this.arrivalPlace = builder.arrivalPlace;
        this.startDate = builder.startDate;
    }

    public int getId() {
        return id;
    }

    public int getActivity() {
        return activity;
    }

    public boolean isLock() {
        return lock;
    }

    public boolean isClose() {
        return close;
    }

    public String getDeparturePlace() {
        return departurePlace;
    }

    public String getArrivalPlace() {
        return arrivalPlace;
    }

    public Date getStartDate() {
        return startDate;
    }

    public RealmList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public RealmList<Waypoint> getWaypoints() {
        return waypoints;
    }

    public static class Builder {
        private int id;
        private int activity;
        private boolean lock;
        private boolean close;
        private String departurePlace;
        private String arrivalPlace;
        private Date startDate;

        public Session build() {
            return new Session(this);
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setActivity(int activity) {
            this.activity = activity;
            return this;
        }

        public Builder setLock(boolean lock) {
            this.lock = lock;
            return this;
        }

        public Builder setClose(boolean close) {
            this.close = close;
            return this;
        }

        public Builder setDeparturePlace(String departurePlace) {
            this.departurePlace = departurePlace;
            return this;
        }

        public Builder setArrivalPlace(String arrivalPlace) {
            this.arrivalPlace = arrivalPlace;
            return this;
        }

        public Builder setStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }
    }
}
