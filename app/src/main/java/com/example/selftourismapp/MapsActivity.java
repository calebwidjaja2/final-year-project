package com.example.selftourismapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.Color;
import android.icu.text.AlphabeticIndex;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";

    private boolean mPermission = false;
    private static final int PERMISSION_REQUEST_CODE = 12345;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final float defaultZoom = 15f;
    private Polyline mPolyLine;
    private LatLng mOrigin;
    private LatLng mDestination;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private MarkerOptions markerOptions;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (mPermission) {
            locateDevice();
            getMyLocation();

            mMap.setMyLocationEnabled(true);


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationPermission();
    }


    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "Moving the camera to : latitude: " + latLng.latitude + ", longitude: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initialMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void locateDevice() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mPermission) {

                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.v(TAG, "oncomplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    defaultZoom);
                        } else {
                            Log.v(TAG, "location is null");
                            Toast.makeText(MapsActivity.this, "Location is unable", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        } catch (SecurityException e) {
            Log.v(TAG, "deviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void locationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mPermission = true;
                initialMap();
            } else {
                ActivityCompat.requestPermissions(this, permission, PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mPermission = false;

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mPermission = false;
                            return;
                        }
                    }
                    mPermission = true;
                    //initialize the map
                    initialMap();
                }
            }
        }
    }
    private void getMyLocation(){
        // getting locationmanager object from system service Location_Service
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationListener = new LocationListener(){
            @Override
            public void onLocationChanged(Location location) {
                mOrigin = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin, 12));
                if (mOrigin != null && mDestination != null)
                    route();
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
        };
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED){
        mMap.setMyLocationEnabled(true);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, mLocationListener);
        // if the person press longer, then it will pop up the route according to the place the person press
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
@Override
public void onMapLongClick(LatLng latLng) {
        mDestination = latLng;
        mMap.clear();
        markerOptions = new MarkerOptions().position(mDestination).title("Your Destination");
        mMap.addMarker(markerOptions);
        if (mOrigin != null && mDestination != null)
        route();
        }
        });
        } else{
        requestPermissions(new String[]{
        Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        }
        }

private void route() {
        // getting the URL to the Google direction API
        String url = getDirectionsURL(mOrigin, mDestination);
        DownloadTask mDownloadTask = new DownloadTask();
        //download json data from the API
        mDownloadTask.execute(url);
        }

private String getDirectionsURL(LatLng origin, LatLng dest) {

        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        //destination of the route
        String strDestionation = "destination=" + dest.latitude + "," + dest.longitude;

        String key = "key=" + getString(R.string.google_maps_key);

        // making parameters
        String paramaters = strOrigin + "&" + strDestionation + "&" + key;
        String output = "json";

        // web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + paramaters;
        return url;

        }

// downloading json data from the URL
private String downloadURL(String strUrl) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
        URL url = new URL(strUrl);

        // make connection to communicate with the url
        urlConnection = (HttpURLConnection) url.openConnection();

        // connecting to the url
        urlConnection.connect();

        // read data inside the URL
        inputStream = urlConnection.getInputStream();

        BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuffer sBuffer = new StringBuffer();

        String line = "";
        while ((line = bReader.readLine()) != null) {
        sBuffer.append(line);
        }
        data = sBuffer.toString();
        bReader.close();
        } catch (Exception e) { // throw an exception
        Log.d("Excception on download", e.toString());
        } finally {
        inputStream.close();
        urlConnection.disconnect();
        }
        return data;

        }

// downloading the data from the URL
private class DownloadTask extends AsyncTask<String, Void, String>{
    @Override
    protected String doInBackground(String... url) {
        // storing data
        String data ="";
        try {
            data = downloadURL(url[0]);
            Log.d("DownloadTask", "DownloadTsak: " + data);
        } catch (Exception e){
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        ParserTask parserTask = new ParserTask();
        parserTask.execute(result);
    }
}
// a class to convert the Google direction into JSON format
private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>{
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try{
            jObject = new JSONObject(jsonData[0]);
            DirectionJSON parser = new DirectionJSON();

            // start converting
            routes = parser.parse(jObject);
        } catch (Exception e){
            e.printStackTrace();
        }
        return routes;
    }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;

        // go through all the points
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = result.get(i);

            // fetching all the points
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);
                points.add(position);
            }
            // adding up all the points to route so it won't be straight line
            lineOptions.addAll(points);
            lineOptions.width(8);
            lineOptions.color(Color.RED);
        }
        // drawing the route
        if (lineOptions != null) {
            if (mPolyLine != null) {
                mPolyLine.remove();
            }
            mPolyLine = mMap.addPolyline(lineOptions);

        } else
            Toast.makeText(getApplicationContext(), "No route is found", Toast.LENGTH_LONG).show();
    }
}
}

