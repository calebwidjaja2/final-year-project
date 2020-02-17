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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static String PERMISSION_FINE = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static String PERMISSION_COARSE = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private final int PERMISSION_CODE = 123;
    boolean Permission = false;
    private GoogleMap gMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    public static final String FINELOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COURSELOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private double currentLat, currentLong;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private static final float DEFAULT_ZOOM = 15f;
    private LatLng mDestination;
    private Polyline mPolyline;
    private MarkerOptions markerOptions;
    private LatLng mOrigin;
    Button Destination;
    Polyline line;
    LatLng singapore;
    LatLng marinaBaySands;
    LatLng chijmes;
    LatLng gardenbythebay;
    LatLng merlion;
    LatLng helixBridge;
    LatLng latlng;
    PlacesClient placesClient;

    private EditText searchText;

    @Override
    protected void onStart() {
        Log.v("onstart", currentLat + "");
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String apiKey = "AIzaSyB7EBtecHKXYRA47sKD1-_rnBUoGbBNRiY";
        Log.v("oncreate", currentLat + "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager() // map fragment
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // setting up the location on the map
        singapore = new LatLng(currentLat, currentLong);
        chijmes = new LatLng(1.2952, 103.8522);
        helixBridge = new LatLng(1.2877, 103.8606);
        marinaBaySands = new LatLng(1.2834, 103.8608);
        merlion = new LatLng(1.2868, 103.8545);
        gardenbythebay = new LatLng(1.2816, 103.8636);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // to enable the Autocomplete Fragment to search the place
        placesClient = Places.createClient(this);
        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // get the latitude longitude of the place
                final LatLng latLng = place.getLatLng();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(new MarkerOptions().position(latLng).title("here!"));

                Log.v("Tag", "onPlaceSelected = " + latLng.latitude + "\n" + latLng.longitude);
            }

            @Override
            public void onError(@NonNull Status status) {
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v("onmapr", currentLat + "");
        mMap = googleMap;
        getMyLocation();
        Log.v("LatLngx", currentLat + "");
        Log.v("LatLngx", currentLong + "");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(singapore));
        mMap.addMarker(new MarkerOptions().position(chijmes).title("Chijmes").
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.addMarker(new MarkerOptions().position(helixBridge).title("Helix Bridge")).
                setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        mMap.addMarker(new MarkerOptions().position(marinaBaySands).title("Marina Bay Sand")).
                setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mMap.addMarker(new MarkerOptions().position(merlion).title("Merlion")).
                setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        mMap.addMarker(new MarkerOptions().position(gardenbythebay).title("Garden By The Bay")).
                setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) { // setting uup the permission

        if (requestCode == 100) {
            if (!verifyPermissions(grantResults)) {
                Toast.makeText(getApplicationContext(), "No sufficient permissions", Toast.LENGTH_LONG).show();
            } else {
                getMyLocation();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean verifyPermissions(int[] grantResults) { // veryfying the permission

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void getMyLocation() {

        // Getting LocationManager object from System Service LOCATION_SERVICE
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mOrigin = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin, 12));
                if (mOrigin != null && mDestination != null)
                    route();
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
        };

        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                mMap.setMyLocationEnabled(true);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, mLocationListener);

                // if the person press longer, then it will pop up the route according to the place the person press
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        mDestination = latLng;
                        mMap.clear();
                        markerOptions = new MarkerOptions().position(mDestination).title("Destination");
                        mMap.addMarker(markerOptions);
                        if (mOrigin != null && mDestination != null)
                            route();
                    }
                });

            } else {
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                }, 100);
            }
        }
    }

    private void route() {

        // getting the URL to the Google direction API
        String url = getDirectionsUrl(mOrigin, mDestination);

        DownloadTask mDownloadTask = new DownloadTask();

        // download json data from the API
        mDownloadTask.execute(url);
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // the origin
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;

        // destination of route
        String strDestination = "destination=" + dest.latitude + "," + dest.longitude;

        String key = "key=" + getString(R.string.google_maps_key);

        // making parameters
        String parameters = strOrigin + "&" + strDestination + "&" + key;

        String output = "json";

        // web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    // downloading json data from the URL
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // make connection to communicate with the url
            urlConnection = (HttpURLConnection) url.openConnection();

            // connecting to the URL
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
            Log.d("Exception on download", e.toString());
        } finally {
            inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // downloading the data from the URL
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // storing data
            String data = "";

            try {
                data = downloadUrl(url[0]); // taking data from the URL
                Log.d("DownloadTask", "DownloadTask : " + data);
            } catch (Exception e) {
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
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {


        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) { // converting the data

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionJSON parser = new DirectionJSON();

                // start converting
                routes = parser.parse(jObject);
            } catch (Exception e) {
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
                if (mPolyline != null) {
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            } else
                Toast.makeText(getApplicationContext(), "No route is found", Toast.LENGTH_LONG).show();
        }
    }
}

