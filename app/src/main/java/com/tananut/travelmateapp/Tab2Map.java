package com.tananut.travelmateapp;

/**
 * Created by Tananut on 9/3/2560.
 */

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.tananut.travelmateapp.R.id.container;
import static com.tananut.travelmateapp.R.id.fadeScreen;
import static com.tananut.travelmateapp.Singleton.REST_API;
import static com.tananut.travelmateapp.Singleton.Tab2;
import static com.tananut.travelmateapp.Singleton.Tab3;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Tab2Map extends Fragment implements LocationListener{

    private MapView _mMapView;
    private GoogleMap googleMap;
    private CameraPosition cameraPosition;
    private PolylineOptions polylineOptions;
    private Polyline polyLine;
    private List<LatLng> routePoints = new ArrayList<>();
    private double longitude = 0.0;
    private double latitude = 0.0;
    private double _current_longitude = 0.0;
    private double _current_latitude = 0.0;
    private double _first_longitude = 0.0;
    private double _first_latitude = 0.0;
    private double _capture_longitude = 0.0;
    private double _capture_latitude = 0.0;
    private LocationManager lm;
    private boolean _isTrack = false;
    private boolean _freeCamMode = false;
    private boolean _chkCreate = false;
    private Location _location;
    private View rootView;
    private GridLayout _buttonCamera;
    private GridLayout _buttonTracking;
    private TextView _buttonText;
    private TextView _buttonTrackingText;
    private File dir;

    public final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TravelMate_Screenshots";
    public String _imageName = "current_";
    public File file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!_chkCreate) {
            rootView = inflater.inflate(R.layout.tab2map, container, false);
            lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            _location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            _buttonCamera = (GridLayout) rootView.findViewById(R.id.button_camera);
            _buttonTracking = (GridLayout) rootView.findViewById(R.id.button_tracking);
            _buttonText = (TextView) _buttonCamera.findViewById(R.id.textView4);
            _buttonTrackingText = (TextView) _buttonTracking.findViewById(R.id.tracking_text);

            _buttonCamera.setVisibility(View.INVISIBLE);
            _buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (_freeCamMode) {
                        _freeCamMode = false;
                        final LatLng current = new LatLng(latitude, longitude);
                        cameraPosition = new CameraPosition.Builder().target(current).build();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(current));
                        _buttonText.setText("CENTER");
                    }
                    else {
                        _freeCamMode = true;
                        _buttonText.setText("FREE-CAM");
                    }
                }
            });

            _buttonTracking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!_isTrack) {
                        _buttonTrackingText.setText("TRACKING - ON");
                        StartHighLightMap();
                    }
                    else {
                        _buttonTrackingText.setText("TRACKING - OFF");
                        StopHighLight();
                    }
                }
            });

            try {
                this.longitude = _location.getLongitude();
                this.latitude = _location.getLatitude();
            } catch (Exception e) {}

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

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);

                    final LatLng current = new LatLng(Tab2().latitude, Tab2().longitude);
                    cameraPosition = new CameraPosition.Builder().zoom(15).target(current).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }
            });
            _chkCreate = true;
        }
        return rootView;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (_isTrack) {
            this.longitude = location.getLongitude();
            this.latitude = location.getLatitude();

            final double finalLongitude = this.longitude;
            final double finalLatitude = this.latitude;

            if (_current_latitude != finalLatitude || _current_longitude != finalLongitude) {
                routePoints.add(new LatLng(this.latitude, this.longitude));
                drawRoute(routePoints);

                if (!_freeCamMode) {
                    final LatLng current = new LatLng(latitude, longitude);
                    cameraPosition = new CameraPosition.Builder().target(current).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(current));
                }
//                Log.d("Polyline : ", "Move");
            }
//                Log.d("Polyline : ", " Not Move");
        }

//        Log.i("Geo_Location", "Latitude: " + latitude + ", Longitude: " + longitude);
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

    public void drawRoute(List<LatLng> location) {
        polylineOptions = new PolylineOptions().width(8).color(0x7F0000FF).addAll(location);
        polyLine = googleMap.addPolyline(polylineOptions);
        polyLine.setPoints(location);
    }

    public void StartHighLightMap() {
        _isTrack = true;
        this._first_latitude = this.latitude;
        this._first_longitude = this.longitude;
        _buttonCamera.setVisibility(View.VISIBLE);
    }

    public void StopHighLight() {
        _isTrack = false;
        String uniqueID = UUID.randomUUID().toString();
        _imageName += uniqueID + ".png";
        this._capture_latitude = (this._first_latitude + this.latitude)/2;
        this._capture_longitude = (this._first_longitude + this.longitude)/2;
        _buttonCamera.setVisibility(View.INVISIBLE);

        Log.d("Latitude : ", "" + this.latitude);
        Log.d("Longtitude : ", "" + this.longitude);
        Log.d("First_Latitude : ", "" + this._first_latitude);
        Log.d("First_Longtitude : ", "" + this._first_longitude);
        Log.d("Capture_Latitude : ", "" + this._capture_latitude);
        Log.d("Capture_Longtitude : ", "" + this._capture_longitude);

        final LatLng current = new LatLng(Tab2()._capture_latitude, Tab2()._capture_longitude);
        cameraPosition = new CameraPosition.Builder().target(current).build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(current));

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Place Explored!\nEnter your topic.");
        final EditText input = new EditText(getActivity());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setIcon(R.drawable.flag);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String _text = input.getText().toString();

                googleMap.addMarker(new MarkerOptions().position(
                        new LatLng(Tab2().latitude, Tab2().longitude)).title("Marker_End"));

                Calendar currenta = Calendar.getInstance();
                String month = (currenta.get(Calendar.MONTH) + 1) < 10? "0" + currenta.get(Calendar.MONTH) : "" + currenta.get(Calendar.MONTH) ;
                String date   = (currenta.get(Calendar.DATE)) < 10? "0" + currenta.get(Calendar.DATE) : "" + currenta.get(Calendar.DATE);
                String hour = (currenta.get(Calendar.HOUR_OF_DAY)) < 10? "0" + currenta.get(Calendar.HOUR_OF_DAY) : "" + currenta.get(Calendar.HOUR_OF_DAY);
                String minute = (currenta.get(Calendar.MINUTE)) < 10? "0" + currenta.get(Calendar.MINUTE) : "" + currenta.get(Calendar.MINUTE);
                String second = (currenta.get(Calendar.SECOND)) < 10? "0" + currenta.get(Calendar.SECOND) : "" + currenta.get(Calendar.SECOND);
                String datetime = "" + currenta.get(Calendar.YEAR) + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
                _text = (_text.equals(""))? datetime : _text;

                String urlParameters  = "uid=1&ename=" + _text +
                        "&des=Click here to edit your description.&time="+ datetime +
                        "&pic_name=" + _imageName +
                        "&latitude=" + Tab2().latitude +
                        "&longitude=" + Tab2().longitude;

                String api = "explored/addexplored";

                try {
                    JSONObject modelReader = REST_API(api, urlParameters);
                    boolean success = modelReader.getBoolean("success");
                    if (!success) {
                        Toast.makeText(getActivity(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                CaptureScreen();

                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(Tab2());
                ft.attach(Tab2());
                ft.commit();
                _chkCreate = false;
                _freeCamMode = false;

                Tab3().refresh();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    public void CaptureScreen() {
        dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        file = new File(dirPath, _imageName);
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                bitmap = snapshot;
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                    Toast.makeText(getActivity(), "Capture Success!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Capture Error!", Toast.LENGTH_SHORT).show();
                }
//                shareImage(_imageName);
            }
        };
        googleMap.snapshot(callback);
    }

    public void shareImage(String image_name){
        file = new File(dirPath, image_name);
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
//            Toast.makeText(Tab2().this, "No App Available", Toast.LENGTH_SHORT).show();
        }
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
