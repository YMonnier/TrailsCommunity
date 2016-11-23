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
    @PrimaryKey
    private int id;
    private double latitude;
    private double longitude;
}
