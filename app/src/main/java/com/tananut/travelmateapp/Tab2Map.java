package com.tananut.travelmateapp;

/**
 * Created by Tananut on 9/3/2560.
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import static com.tananut.travelmateapp.R.id.container;
import static com.tananut.travelmateapp.Singleton.Tab2;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private Location _location;
    private View rootView;
    private boolean _chkCreate = false;
    private String _imageName = "current_";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!_chkCreate) {
            rootView = inflater.inflate(R.layout.tab2map, container, false);
            lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            _location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            try {
                longitude = _location.getLongitude();
                latitude = _location.getLatitude();
            } catch (Exception e) {
            }

            _current_longitude = this.longitude;
            _current_latitude = this.latitude;

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

                    final LatLng current = new LatLng(latitude, longitude);
                    cameraPosition = new CameraPosition.Builder().zoom(16).target(current).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }
            });
            _chkCreate = true;
        }
        return rootView;
    }

    @Override
    public void onLocationChanged(Location location) {

        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();

        final double finalLongitude = this.longitude;
        final double finalLatitude = this.latitude;

        if (_isTrack) {
            if (_current_latitude != finalLatitude || _current_longitude != finalLongitude) {
                Polyline line = this.googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(_current_latitude, _current_longitude), new LatLng(finalLatitude, finalLongitude))
                        .width(15)
                        .color(0x7F0000FF));
                _current_latitude = finalLatitude;
                _current_longitude = finalLongitude;
                final LatLng current = new LatLng(latitude, longitude);
                cameraPosition = new CameraPosition.Builder().target(current).zoom(17.0f).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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

    }

    public void StopHighLight() {
        _isTrack = false;

        final double finalLongitude = this.longitude;
        final double finalLatitude = this.latitude;

        googleMap.addMarker(new MarkerOptions().position(
                new LatLng(finalLatitude, finalLongitude)).title("Marker_End"));

        CaptureScreen();
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(Tab2());
        ft.attach(Tab2());
        ft.commit();
        _chkCreate = false;
//        new AlertDialog.Builder(getActivity())
//                .setTitle("Alert")
//                .setMessage("Screen shot Completed!")
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
    }

    public void CaptureScreen() {
        String uniqueID = UUID.randomUUID().toString();
        _imageName += uniqueID + ".png";
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TravelMate_Screenshots";
                File dir = new File(dirPath);
                if(!dir.exists())
                    dir.mkdirs();
                File file = new File(dirPath, _imageName);
                bitmap = snapshot;
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                    Toast.makeText(getActivity(), "Capture OK", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Capture NOT OK", Toast.LENGTH_LONG).show();
                }
            }
        };
        googleMap.snapshot(callback);
//        _chkCreate = false;
    }

//    public void RenewGoogleMap()
//    {
//        _current_longitude = this.longitude;
//        _current_latitude = this.latitude;
//
//        _mMapView = (MapView) rootView.findViewById(R.id.googleMap);
////        _mMapView.onCreate(savedInstanceState);
////
//        _mMapView.onResume(); // needed to get the map to display immediately
//
//        try {
//            MapsInitializer.initialize(getActivity().getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        _mMapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap mMap) {
//                googleMap = mMap;
//
//                // For showing a move to my location button
//                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    googleMap.setMyLocationEnabled(true);
//                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//                    return;
//                }
//                googleMap.setMyLocationEnabled(true);
//
//                final LatLng current = new LatLng(latitude, longitude);
//                cameraPosition = new CameraPosition.Builder().zoom(16).target(current).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//            }
//        });
//    }

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
