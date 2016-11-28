package fr.univ_tln.trailscommunity.features.session.navigation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;
import java.util.Map;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.features.session.SessionActivity;
import fr.univ_tln.trailscommunity.models.Coordinate;
import fr.univ_tln.trailscommunity.models.Session;

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
        GoogleMap.OnMapLongClickListener,
        LocationListener {

    @RootContext
    SessionActivity context;

    /**
     *
     */
    GoogleMap mapView;

    LocationManager locationManager;

    private Map<Integer, String> users;

    /**
     *
     */
    public void init(Session session) {
        Log.d(MapNavigation.class.getName(), "init");
        Log.d(MapNavigation.class.getName(), context.toString());


        SupportMapFragment mapFragment = (SupportMapFragment) context.getSupportFragmentManager()
                .findFragmentById(R.id.map_navigation);
        mapFragment.getMapAsync(this);

        Session.TypeActivity typeActivity = Session.TypeActivity.values()[session.getActivity()];
        LocationSetting locationSetting = LocationSetting.fromActivity(typeActivity);

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    locationSetting.getTime(),
                    locationSetting.getDistance(), this);
        }

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
        Log.d(MapNavigation.class.getName(), "onMapReady");
        mapView = googleMap;

        //mapView.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        addCoordinate(new Coordinate());
    }

    /**
     * Add a new waypoint to the current map.
     * @param coordinate waypoint coordinate.
     */
    public void addWaypoint(final LatLng coordinate) {
        mapView.addMarker(new MarkerOptions().position(coordinate).title("Pseudo | Waypoint"));
    }

    public void addCoordinate(final Coordinate coordinate) {
        //mapView.addPolyline()
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(37.35, -122.0))
                .add(new LatLng(37.45, -122.0))  // North of the previous point, but at the same longitude
                .add(new LatLng(37.45, -122.2))  // Same latitude, and 30km to the west
                .add(new LatLng(37.35, -122.2))  // Same longitude, and 16km to the south
                .add(new LatLng(37.35, -122.0)); // Closes the polyline.

        // Get back the mutable Polyline
        Polyline polyline = mapView.addPolyline(rectOptions);
        polyline.setColor(Color.BLUE);
        addUserMarker(polyline);
        mapView.moveCamera(CameraUpdateFactory.newLatLng(polyline.getPoints().get(0)));
        //polyline.setPoints();
    }

    public void addCoordinate(final List<Coordinate> coordinates) {

    }

    private void addUserMarker(Polyline polyline){
        mapView.addMarker(new MarkerOptions()
                .title("Pseudo")
                .position(polyline.getPoints().get(polyline.getPoints().size() - 1))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();
    }

    /**
     * Long
     * @param latLng
     */
    @Override
    public void onMapLongClick(LatLng latLng) {
        addWaypoint(latLng);
    }

    //
    //
    // Location Listener
    //
    //

    @Override
    public void onLocationChanged(Location location) {
        Log.d(MapNavigation.class.getName(), "Location updated: " + location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
