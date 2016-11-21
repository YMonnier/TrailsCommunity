package fr.univ_tln.trailscommunity.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.models.
 * File User.java.
 * Created by Ysee on 21/11/2016 - 15:36.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class User extends RealmObject {
    @PrimaryKey
    private int id;
    private String nickname;
    private String email;
    private String password;
}
