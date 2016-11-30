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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.features.session.SessionActivity;
import fr.univ_tln.trailscommunity.features.session.navigation.location.LocationService;
import fr.univ_tln.trailscommunity.features.session.navigation.location.LocationService_;
import fr.univ_tln.trailscommunity.features.session.navigation.location.LocationSettings;
import fr.univ_tln.trailscommunity.models.Coordinate;
import fr.univ_tln.trailscommunity.models.Session;
import fr.univ_tln.trailscommunity.models.Waypoint;
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

    private static final String TAG = MapNavigation.class.getSimpleName();

    @RootContext
    SessionActivity context;

    /**
     * Map view
     */
    GoogleMap mapView;


    private Map<Integer, String> users;

    private Polyline polyline;

    /**
     * Gson instance use to map json to object
     * or to convert object to json.
     * Used for `intent` communication.
     */
    private Gson gson;

    /**
     *
     */
    public void init(Session session) {
        Log.d(TAG, "init");
        gson = new Gson();
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

        Session.TypeActivity typeActivity = Session.TypeActivity.values()[session.getActivity()];
        LocationSettings locationSetting = LocationSettings.fromActivity(typeActivity);

        // Start the location service
        // See fr.univ_tln.trailscommunity.features.session.navigation.LocationService
        LocationService_.intent(context)
                .extra(LocationSettings.EXTRA_DISTANCE, locationSetting.getDistance())
                .extra(LocationSettings.EXTRA_TIME_MILLIS, locationSetting.getTime())
                .start();
    }

    /**
     * Method to stop observer broadcast
     * messaging and service location.
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

        //mapView.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //
        // Test
        //

        PolylineOptions rectOptions = new PolylineOptions();
        polyline = mapView.addPolyline(rectOptions);
        polyline.setColor(Color.RED);
        polyline.setVisible(true);
        polyline.setWidth(6);
        polyline.setZIndex(1234);
        //addUserMarker(polyline);
    }

    /**
     * Add a new waypoint to the current map.
     *
     * @param coordinate waypoint coordinate.
     */
    public void addWaypoint(final LatLng coordinate) {
        mapView.addMarker(new MarkerOptions().position(coordinate).title("Pseudo | Waypoint"));
    }

    /**
     * Add a new point to the specific polyline.
     *
     * @param coordinate coordinate we want to add.
     */
    public void addCoordinate(final Coordinate coordinate) {
        if (coordinate == null)
            throw new AssertionError("coordinate should not be null");

        if (coordinate != null) {
            LatLng point = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
            List<LatLng> listPoint = polyline.getPoints();
            if (listPoint != null) {
                listPoint.add(point);
                polyline.setPoints(listPoint);
                polyline.setVisible(true);
                mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 18));
            }
        }
    }

    public void addCoordinate(final List<Coordinate> coordinates) {

    }

    private void addUserMarker(Polyline polyline) {
        mapView.addMarker(new MarkerOptions()
                .title("Pseudo")
                .position(polyline.getPoints().get(polyline.getPoints().size() - 1))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();
    }

    /**
     * Long click which will add a new waypoint and broadcast
     * to others participant through the server.
     *
     * @param latLng location waypoint
     */
    @Override
    public void onMapLongClick(LatLng latLng) {
        addWaypoint(latLng);
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
            addCoordinate(coordinate);
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
