package fr.univ_tln.trailscommunity.models;

import java.util.Date;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.models.
 * File Session.java.
 * Created by Ysee on 19/11/2016 - 14:04.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class Session {
    private int activity;
    private boolean lock;
    private String departurePlace;
    private String arrivalPlace;
    private Date startDate;
    private boolean close;

    public Session(Builder builder) {
        this.activity = builder.activity;
        this.lock = builder.lock;
        this.departurePlace = builder.departurePlace;
        this.arrivalPlace = builder.arrivalPlace;
        this.startDate = builder.startDate;
        this.close = builder.close;
    }

    public boolean isLock() {
        return lock;
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

    public boolean isClose() {
        return close;
    }

    public int getActivity() {
        return activity;
    }

    public String getActivityName() {
        switch (activity) {
            case 1:
                return "Hiking";
            case 2:
                return "Bicycle";
            case 3:
                return "4x4";
            default:
                return "Empty";
        }
    }

    public static class Builder {
        private int activity;
        private boolean lock;
        private String departurePlace;
        private String arrivalPlace;
        private Date startDate;
        private boolean close;

        public Session build() {
            return new Session(this);
        }

        public Builder setActivity(int activity) {
            this.activity = activity;
            return this;
        }

        public Builder setLock(boolean lock) {
            this.lock = lock;
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

        public Builder setClose(boolean close) {
            this.close = close;
            return this;
        }
    }
}
