package com.tananut.travelmateapp;

/**
 * Created by Tananut on 9/3/2560.
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class Tab2Map extends Fragment implements LocationListener{

    private MapView _mMapView;
    private GoogleMap googleMap;
    private CameraPosition cameraPosition;
    //    private List<LatLng> routePoints = new ArrayList<>();
    private double longitude = 0.0;
    private double latitude = 0.0;
    private double _current_longitude = 0.0;
    private double _current_latitude = 0.0;
    private LocationManager lm;
    private boolean _isTrack = false;
//    private Location location;
    private Thread _tracking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2map, container, false);
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  this);

//        try {
//            longitude = location.getLongitude();
//            latitude = location.getLatitude();
//        } catch (Exception e) {
//        }
        final LatLng current = new LatLng(latitude, longitude);

        _mMapView = (MapView) rootView.findViewById(R.id.googleMap);
        _mMapView.onCreate(savedInstanceState);

        _mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        _mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                cameraPosition = new CameraPosition.Builder().target(current).zoom(16).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });

        return rootView;
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();

        final double finalLongitude = this.longitude;
        final double finalLatitude = this.latitude;

        if (_isTrack) {
            if (_current_latitude != finalLatitude || _current_longitude != finalLongitude) {
                Polyline line = this.googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(finalLatitude, finalLongitude), new LatLng(finalLatitude, finalLongitude))
                        .width(15)
                        .color(0x7F0000FF));
                _current_latitude = finalLatitude;
                _current_longitude = finalLongitude;
                Log.d("Polyline : ", "Move");
            } else
                Log.d("Polyline : ", " Not Move");
        }

        Log.i("Geo_Location", "Latitude: " + latitude + ", Longitude: " + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void StartHighLightMap() {
        _isTrack = true;
        final double finalLongitude = this.longitude;
        final double finalLatitude = this.latitude;

        googleMap.addMarker(new MarkerOptions().position(
                new LatLng(finalLatitude, finalLongitude)).title("Marker_Start"));

//        final int[] i = {0};
//        _tracking = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    while (true) {
//                        _tracking.sleep(3000);
//                        Log.d("Test Threading!", "i = " + i[0]);
//                        HighLightMap();
//                        i[0]++;
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        _tracking.start();
    }

    public void StopHighLight() {
        _isTrack = false;
//        _tracking.interrupt();

        final double finalLongitude = this.longitude;
        final double finalLatitude = this.latitude;

        googleMap.addMarker(new MarkerOptions().position(
                new LatLng(finalLatitude, finalLongitude)).title("Marker_End"));
    }

    @Override
    public void onResume() {
        super.onResume();
        _mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        _mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        _mMapView.onLowMemory();
    }
}
