package fr.univ_tln.trailscommunity.features.session.navigation;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.features.session.SessionActivity;
import fr.univ_tln.trailscommunity.features.session.navigation.location.LocationService;
import fr.univ_tln.trailscommunity.features.session.navigation.location.LocationService_;
import fr.univ_tln.trailscommunity.features.session.navigation.location.LocationSettings;
import fr.univ_tln.trailscommunity.models.Coordinate;
import fr.univ_tln.trailscommunity.models.Session;
import fr.univ_tln.trailscommunity.models.UserNavigationSettings;
import fr.univ_tln.trailscommunity.models.Waypoint;
import fr.univ_tln.trailscommunity.utilities.color.ColorUtils;
import fr.univ_tln.trailscommunity.utilities.json.GsonSingleton;
import fr.univ_tln.trailscommunity.utilities.mapview.MapViewUtils;
import fr.univ_tln.trailscommunity.utilities.network.TCRestApi;
import fr.univ_tln.trailscommunity.utilities.notification.NotificationReceiverService;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.features.session.navigation.
 * File MapNavigation.java.
 * Created by Ysee on 28/11/2016 - 14:39.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

/**
 * Map navigation facade to manipulate a google map
 */
@EBean
public class MapNavigation
        implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener {

    /**
     * Tag used for Logger.
     */
    private static final String TAG = MapNavigation.class.getSimpleName();

    /**
     * Width of polyline.
     */
    private static final int WIDTH_POLYLINE = 6;

    /**
     * Constant used to define
     * the current user color marker.
     */
    private static final String CURRENT_USER_COLOR = "#1D4A64";

    @RootContext
    SessionActivity context;

    /**
     * Rest service to get
     * information from server.
     */
    @RestService
    TCRestApi tcRestApi;

    /**
     * Map view
     */
    GoogleMap mapView;


    /**
     * Used to manage users connected to the current session.
     * We can easily add user location, move the
     * user maker or change some settings.
     * See `UserNavigationSettings.java`
     */
    private Map<Integer, UserNavigationSettings> users;


    /**
     * Current user polyline
     */
    private UserNavigationSettings currentUser;

    /**
     * Gson instance use to map json to object
     * or to convert object to json.
     * Used for `intent` communication.
     */
    private Gson gson;

    /**
     * Current session
     */
    private Session session;

    /**
     * Initialize the Map Navigation and the Location Service.
     * Add all broadcast observe to receive location
     * from LocationService et some information from Push Notification.
     */
    public void init(Session session) {
        Log.d(TAG, "init");
        this.session = session;
        this.users = new HashMap<>();
        this.gson = GsonSingleton.getInstance();

        // Get Google Map and initialize it
        SupportMapFragment mapFragment = (SupportMapFragment) context.getSupportFragmentManager()
                .findFragmentById(R.id.map_navigation);
        mapFragment.getMapAsync(this);

        // Add observer broadcast messaging for location.
        LocalBroadcastManager.getInstance(context).registerReceiver(locationReceiver,
                new IntentFilter(LocationService.LOCATION_BROADCAST));

        // Add observer broadcast to ask location settings id gps is disabled.
        LocalBroadcastManager.getInstance(context).registerReceiver(askLocationReceiver,
                new IntentFilter(LocationService.ASK_LOCATION_SETTINGS_BROADCAST));

        // Add observer broadcast to receive location from notification
        LocalBroadcastManager.getInstance(context).registerReceiver(coordinateSharingReceiver,
                new IntentFilter(NotificationReceiverService.COORDINATE_SHARING_RECEIVER));

        // Add observer broadcast to receive waypoint from notification
        LocalBroadcastManager.getInstance(context).registerReceiver(waypointSharingReceiver,
                new IntentFilter(NotificationReceiverService.WAYPOINT_SHARING_RECEIVER));

        // Get activity information-
        // Start the location service
        // See fr.univ_tln.trailscommunity.features.session.navigation.LocationService

        Session.TypeActivity typeActivity = Session.TypeActivity.values()[session.getActivity()];
        LocationSettings locationSetting = LocationSettings.fromActivity(typeActivity);

        LocationService_.intent(context)
                .extra(LocationSettings.EXTRA_DISTANCE, locationSetting.getDistance())
                .extra(LocationSettings.EXTRA_TIME_MILLIS, locationSetting.getTime())
                .start();
    }

    /**
     * Method to stop observer broadcast messaging and service location.
     * Used when use leave application from session activity(onFinish`, `onDestroy`, `onStop`)
     */
    public void stop() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(locationReceiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(askLocationReceiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(waypointSharingReceiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(coordinateSharingReceiver);
        LocationService_.intent(context)
                .stop();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.setOnMapLongClickListener(this);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        Log.d(TAG, "onMapReady");
        mapView = googleMap;
        setupCurrentUser();
        //mapView.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * Initialize the current user by creating a
     * UserNavigationSettings with default marker, color..
     */
    private void setupCurrentUser() {
        currentUser = new UserNavigationSettings.Builder()
                .setColor(CURRENT_USER_COLOR)
                .setId(fr.univ_tln.trailscommunity.Settings.userId)
                .setMarker(createMarker(CURRENT_USER_COLOR, null, false))
                .setPolyline(createPolyline(CURRENT_USER_COLOR, null))
                .build();
    }

    /**
     * Add a new waypoint to the current map.
     *
     * @param latLng waypoint coordinate.
     */
    private void addWaypoint(final LatLng latLng) {
        Coordinate coordinate = new Coordinate.Builder()
                .setLatitude(latLng.latitude)
                .setLongitude(latLng.longitude)
                .build();

        mapView.addMarker(new MarkerOptions().position(latLng).title("Pseudo | Waypoint"));
        shareWaypoint(coordinate);
    }

    @Background
    void shareWaypoint(final Coordinate coordinate) {
        try {
            tcRestApi.setHeader(fr.univ_tln.trailscommunity.Settings.AUTHORIZATION_HEADER_NAME, fr.univ_tln.trailscommunity.Settings.TOKEN_AUTHORIZATION);
            tcRestApi.shareWaypoint(session.getId(), coordinate);
            //Log.d(TAG, response.toString());
            Log.d(TAG, "addWaypoint OK");
        } catch (RestClientException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    /**
     * Add a new point to the specific currentPolyline.
     *
     * @param coordinate coordinate we want to add.
     */
    private void addCoordinate(final UserNavigationSettings user, final Coordinate coordinate) {
        if (coordinate == null || user == null)
            throw new AssertionError("coordinate or user should not be null");

        if (coordinate != null && user != null) {
            LatLng point = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
            List<LatLng> listPoint = user.getPolyline().getPoints();
            if (listPoint != null) {
                listPoint.add(point);
                user.getPolyline().setPoints(listPoint);
                user.getPolyline().setVisible(true);
                user.getMarker().setPosition(point);
                user.getMarker().setVisible(true);
                //mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 18));
            }
        }
    }

    @Background
    void shareCoordinate(final int sessionId, final Coordinate coordinate) {
        try {
            tcRestApi.setHeader(fr.univ_tln.trailscommunity.Settings.AUTHORIZATION_HEADER_NAME, fr.univ_tln.trailscommunity.Settings.TOKEN_AUTHORIZATION);
            tcRestApi.shareCoordinate(sessionId, coordinate);
            Log.d(TAG, "locationReceiver OK");
        } catch (RestClientException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    /**
     * Long click which will add a new waypoint and broadcast
     * to others participant through the server.
     *
     * @param latLng location waypoint
     */
    @Override
    public void onMapLongClick(final LatLng latLng) {
        addWaypoint(latLng);
    }

    /**
     * Create a new Polyline and add it to the current map view.
     *
     * @param color      polyline color
     * @param coordinate first point into poline
     * @return Polyline create by the MapView
     */
    private Polyline createPolyline(final String color, final Coordinate coordinate) {
        PolylineOptions polylineOptions = new PolylineOptions();

        if (coordinate != null)
            polylineOptions.add(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()));

        Polyline polyline = mapView.addPolyline(polylineOptions);
        polyline.setColor(Color.parseColor(color));
        polyline.setVisible(true);
        polyline.setWidth(WIDTH_POLYLINE);
        return polyline;
    }

    /**
     * CReate a new marker and add it to the current map view.
     *
     * @param color      marker color
     * @param coordinate marker position
     * @param status     visible status. If true the marker is visible, otherwise, false.
     * @return Marker created by the MapView
     */
    private Marker createMarker(final String color, final Coordinate coordinate, final boolean status) {
        MarkerOptions markerOptions = new MarkerOptions()
                .visible(status)
                .icon(MapViewUtils.getMarkerIcon(color));
        if (coordinate != null)
            markerOptions
                    .position(
                            new LatLng(coordinate.getLatitude(),
                                    coordinate.getLongitude()));
        else
            markerOptions.position(new LatLng(0.0, 0.0));

        return mapView.addMarker(markerOptions);
    }

    /**
     * Handler for receive location intents. This will be
     * called whenever an Intent with an action named `LocationService.LOCATION_BROADCAST`
     * is broadcasted.
     */
    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonLocation = intent.getStringExtra(LocationService_.EXTRA_LOCATION);
            Coordinate coordinate = gson.fromJson(jsonLocation, Coordinate.class);
            Log.d(TAG, "Got location from service: " + coordinate);

            if (currentUser == null)
                throw new AssertionError("current user should not be null");

            if (currentUser != null) {
                addCoordinate(currentUser, coordinate);

                shareCoordinate(session.getId(), coordinate);
            }
        }
    };

    /**
     * Handler for receive the ask location settings. This will be
     * called whenever an Intent with an action named `LocationService.ASK_LOCATION_SETTINGS`
     * is broadcasted.
     */
    private BroadcastReceiver askLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showSettingsAlert();
        }
    };

    /**
     * Handler for receive the location intent from notification(NotificationReceiverService). This will be
     * called whenever an Intent with an action named `LocationService.ASK_LOCATION_SETTINGS`
     * is broadcasted.
     */
    private BroadcastReceiver coordinateSharingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String json = intent.getStringExtra(NotificationReceiverService.EXTRA_COORDINATE_SHARING_RECEIVER);
            Coordinate coordinate = gson.fromJson(json, Coordinate.class);
            Log.d(TAG, "Got location from notification: " + coordinate);

            if (users == null)
                throw new AssertionError("Users map should not be null");

            if (users != null) {
                int userId = coordinate.getUserId();
                if (users.containsKey(userId)) {
                    addCoordinate(users.get(userId), coordinate);
                } else {
                    // Add the new user to the map
                    String color = ColorUtils.randomHex();
                    UserNavigationSettings user = new UserNavigationSettings.Builder()
                            .setColor(color)
                            .setId(userId)
                            .setPolyline(createPolyline(color, coordinate))
                            .setMarker(createMarker(color, coordinate, true))
                            .build();
                    users.put(user.getId(), user);
                    addCoordinate(user, coordinate);
                }
            }
        }
    };

    /**
     * Handler for receive the waypoint intent from notification(NotificationReceiverService). This will be
     * called whenever an Intent with an action named `LocationService.ASK_LOCATION_SETTINGS`
     * is broadcasted.
     */
    private BroadcastReceiver waypointSharingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String json = intent.getStringExtra(NotificationReceiverService.EXTRA_WAYPOINT_SHARING_RECEIVER);
            Waypoint waypoint = gson.fromJson(json, Waypoint.class);
            Log.d(TAG, "Got waypoint from notification: " + waypoint);
        }
    };

    /**
     * Function to show settings alert dialog.
     * On pressing the Settings button it will launch Settings Options.
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                dialog.cancel();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
