package fr.univ_tln.trailscommunity.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import fr.univ_tln.trailscommunity.utilities.exceptions.TypeActivityEnumException;
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

    @SerializedName("departure_place")
    private String departurePlace;

    @SerializedName("arrival_place")
    private String arrivalPlace;

    @SerializedName("start_date")
    private String startDate;

    private String password;
    private RealmList<Coordinate> coordinates;
    private RealmList<Waypoint> waypoints;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("updatedAt")
    private Date updatedAt;

    /**
     * Enumeration of activities type.
     */
    public enum TypeActivity {
        OFFROAD4x4("Off road 4x4"), // start index 0
        HIKING("Hiking"),
        RUNNING("Running"),
        PAINTBALL("Paintball"),
        BICYCLE("Bicycle"),
        BOAT("Boat"); // end index

        private final String name;

        TypeActivity(String name) {
            this.name = name;
        }

        /**
         * Return the enum index
         * depending on his name.
         *
         * @param name name activity
         * @return enum value index
         * @throws TypeActivityEnumException throw a new exception
         *                                   if the enum value is not found
         */
        public static int getIndex(String name) throws TypeActivityEnumException {
            int res = -1;
            if (name == null)
                throw new AssertionError("Name should not be null");
            if (!name.isEmpty()) {
                try {
                    TypeActivity ta = valueOf(name.toUpperCase());
                    if (ta == null)
                        throw new AssertionError("Name should not be null");
                    if (ta != null)
                        res = ta.ordinal();
                } catch (IllegalArgumentException e) {
                    throw new TypeActivityEnumException("Name not found");
                }
            }
            return res;
        }

        public String toString() {
            return this.name;
        }
    }


    public Session() {
    }

    public Session(Builder builder) {
        this.id = builder.id;
        this.activity = builder.activity;
        this.lock = builder.lock;
        this.password = builder.password;
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

    public String getActivityName() {
        switch (activity) {
            case 0:
                return "4X4";
            case 1:
                return "Hiking";
            case 2:
                return "Running";
            case 3:
                return "Paintball";
            case 4:
                return "Bicycling";
            case 5:
                return "Boat";

            default:
                return "";
        }
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

    public String getStartDate() {
        return startDate;
    }

    public RealmList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public RealmList<Waypoint> getWaypoints() {
        return waypoints;
    }

    public String getPassword() {
        return password;
    }

    public void setCreated_at(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdated_at(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreated_at() {
        return createdAt;
    }

    public Date getUpdated_at() {
        return updatedAt;
    }

    public static class Builder {
        private int id;
        private int activity;
        private boolean lock;
        private boolean close;
        private String password;
        private String departurePlace;
        private String arrivalPlace;
        private String startDate;

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

        public Builder setPassword(String password) {
            this.password = password;
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

        public Builder setStartDate(String startDate) {
            this.startDate = startDate;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Session{" +
                "activity=" + activity +
                ", id=" + id +
                ", lock=" + lock +
                ", close=" + close +
                ", departurePlace='" + departurePlace + '\'' +
                ", arrivalPlace='" + arrivalPlace + '\'' +
                ", startDate='" + startDate + '\'' +
                ", password='" + password + '\'' +
                ", coordinates=" + coordinates +
                ", waypoints=" + waypoints +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
