package fr.univ_tln.trailscommunity.models;

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
    private int user_id;
    private Date date;
}
