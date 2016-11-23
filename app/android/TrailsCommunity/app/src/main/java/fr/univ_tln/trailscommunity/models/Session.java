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
    private String departurePlace;
    private String arrivalPlace;
    private Date startDate;
    private boolean close;
    private RealmList<Coordinate> coordinates;
    private RealmList<Waypoint> waypoints;
}
