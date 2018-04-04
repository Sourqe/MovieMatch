package nl.tue.moviematch;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap; // the google map that is being used
    private GoogleApiClient mGoogleApiClient; // the google api client
    private Location mLastLocation;  // the most recent location
    private Marker mCurrLocationMarker; // the marker of the current location
    private LocationRequest mLocationRequest; // the location of the request
    private int PROXIMITY_RADIUS = 10000; // the radius used when looking for places
    private double latitude, longitude; // the latitude and longitude of a location
    private double newLatitude, newLongitude; // the new latitude and longitude of current loc.
    private double endLatitude, endLongitude; // the new latitude and longitude of a location
    private LatLng mOriginalMarker; // the location of the original current location marker
    private boolean doubleClick = false; // check if we have a double click
    private MarkerOptions markerOptionsCurrent; // the marker options we are using
    private double orLatitude, orLongitude; // the original latitude and longitude of current loc.
    private LatLng orLatLng; // the original latLng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content of the activity
        setContentView(R.layout.activity_maps);
        // Set the seekbar from the layout
        SeekBar seekBar = (SeekBar) findViewById(R.id.maxDistBar);
        // Set the textView from the layout, going to be used for the max distance
        final TextView textView = (TextView) findViewById(R.id.maxDistance);
        Button search = (Button) findViewById(R.id.B_search);
        Button navigate = (Button) findViewById(R.id.B_navigate);

        // Check if we have a functioning network available
        if (isNetworkAvailable() == false) {
            Toast.makeText(MapsActivity.this, "No network found! Please make sure " +
                            "you are connect to a network before using this function of the app.",
                    Toast.LENGTH_SHORT).show();
            // disable the buttons so that they cannot be pressed anymore
            search.setEnabled(false);
            navigate.setEnabled(false);
        } else { // if we do have a network
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Check if we have access to the GPS
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // disable the buttons so that they cannot be pressed anymore
                search.setEnabled(false);
                navigate.setEnabled(false);
                // invoke method that displays error
                showGPSDisabledAlertToUser();
            } else { // If we do have access to the GPS
                // Check location premission if the android os version is high enough
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkLocationPermission();
                }

                //Check if Google Play Services Available or not
                if (!CheckGooglePlayServices()) {
                    Log.d("onCreate", "Finishing test case since Google Play Services " +
                            "are not available");
                    finish();
                } else {
                    Log.d("onCreate", "Google Play Services available.");
                }

                // Changing statusbar color
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    Window window = this.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(this.getResources().getColor(R.color.darkergreenish));
                }

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

                // Listener for the seekbar, registers all movement seekbar related
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        // set the text equivalent to the amount of km that we maximally allow
                        textView.setText(i + " Km");
                        // set the proximity radius used in the API equivalent to that amount of KM in M
                        PROXIMITY_RADIUS = i * 1000;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // required empty methods
                        return;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // required empty methods
                        return;
                    }
                });
            }
        }
    }

    private boolean isNetworkAvailable() {
        // check if network is available
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void showGPSDisabledAlertToUser(){
        // show the user that their GPS is disabled with a dialog interface
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. " +
                "Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Go to your Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private boolean CheckGooglePlayServices() {
        // Check if the google play services is available, if not show error message
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);

        // Check if our connection result is a succes or not
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
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
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if we have permission
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);

    }

    protected synchronized void buildGoogleApiClient() {
        // Build the google api client and connect it
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void onClick(View v) {
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        // Check which button gets pressed
        switch(v.getId()) {
            // the search button gets pressed
            case R.id.B_search: {
                // clear the map
                mMap.clear();
                // add the current position marker again
                mCurrLocationMarker = mMap.addMarker(markerOptionsCurrent);
                // retrieve the location from the textField
                EditText tf_location = (EditText) findViewById(R.id.TF_location);
                // set the location to the retrieved location
                String location = tf_location.getText().toString();
                // initialize the addressList
                List<Address> addressList = null;
                // set markerOptions as a MarkerOptions type
                MarkerOptions markerOptions = new MarkerOptions();
                Log.d("location = ", location);

                // if the manual location has been filled in
                if (!location.equals("")) {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        // set the addresslist with a maximum of 5 result
                        // use the location as String name (check documentation)
                        addressList = geocoder.getFromLocationName(location, 5);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // if no places found, display a message for the usesr, so they know
                    if (addressList.size() == 0) {
                        Log.d("places", "no places found");
                        Toast.makeText(MapsActivity.this, "No results found. " +
                                    "Try again searching again for another place.",
                                Toast.LENGTH_SHORT).show();
                    }

                    // if we have found nearby places
                    if (addressList != null) {
                        // for each place in the list
                        for (int i = 0; i < addressList.size(); i++) {
                            Address myAddress = addressList.get(i);
                            LatLng latLng = new LatLng(myAddress.getLatitude(),
                                    myAddress.getLongitude());
                            // set the location of the marker to the current location of the place
                            markerOptions.position(latLng);
                            // set the title of that place
                            markerOptions.title(addressList.get(i).getFeatureName());
                            // move the camera to that place
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            // add a marker for that location
                            mMap.addMarker(markerOptions);
                            // get the first location
                            Address locationSearch = addressList.get(0);
                            // set the longitude and latitude of that place
                            newLongitude = locationSearch.getLongitude();
                            newLatitude = locationSearch.getLatitude();
                            // set the dataTransfer
                            dataTransfer = new Object[2];
                            String theater = "movie_theater";
                            // get the url, using the latitude, longitude, and places type
                            String url = getUrl(newLatitude, newLongitude, theater);
                            // get the nearby places data (see GetNearbyPlacesData.java)
                            getNearbyPlacesData = new GetNearbyPlacesData();
                            dataTransfer[0] = mMap;
                            dataTransfer[1] = url;
                            getNearbyPlacesData.execute(dataTransfer);
                            Toast.makeText(MapsActivity.this, "Showing Movie " +
                                    "Theaters near: "+location, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // add aditional variables so that we actually zoom in to the right position,
                    // without this it would zoom in too fast and go to the wrong spot.
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(orLatLng, 10);
                    mMap.animateCamera(cameraUpdate);
                    Log.d("location", "empty");

                    // repeat the same process as above
                    dataTransfer = new Object[2];
                    String theater = "movie_theater";
                    String url = getUrl(orLatitude, orLongitude, theater);
                    getNearbyPlacesData = new GetNearbyPlacesData();
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;
                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby " +
                            "Movie Theaters", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            break;
            case R.id.B_navigate:
                // get the location of the current position
                LatLng currentPosition = mCurrLocationMarker.getPosition();
                Log.d("location", currentPosition.toString());
                Log.d("location", mOriginalMarker.toString());

                // check if the current location is equivalent to the original location
                if (currentPosition.equals(mOriginalMarker)) {
                    // inform the user that they first have to move the current pos. marker
                    Toast.makeText(MapsActivity.this, "Move the current location " +
                            "marker first before navigating somewhere", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    dataTransfer = new Object[3];
                    String url = getDirectionsUrl();
                    GetDirectionsData getDirectionsData = new GetDirectionsData();
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;
                    dataTransfer[2] = new LatLng(endLatitude, endLongitude);
                    getDirectionsData.execute(dataTransfer);
                    break;
                }
        }
    }

    private String getDirectionsUrl() {
        // get the url to the directions, using the origin and destionation
        StringBuilder googleDirectionsUrl = new StringBuilder
                ("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
        googleDirectionsUrl.append("&destination="+endLatitude+","+endLongitude);
        googleDirectionsUrl.append("&key="+"AIzaSyCAcfy-02UHSu2F6WeQ1rhQhkCr51eBL9g");
        return googleDirectionsUrl.toString();
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        // get the url for the places using the location, radius, and type
        StringBuilder googlePlacesUrl = new StringBuilder
                ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyBj-cnmMUY21M0vnIKz0k3tD3bRdyZea-Y");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnected(Bundle bundle) {
        // When we first connect with the map, configure all the settings
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates
                    (mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // display some text to inform the user
        Toast.makeText(MapsActivity.this,"Your connection with the application " +
                        "has been suspended",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");
        // Set the last location
        mLastLocation = location;

        // Check if we have the marker. If so, remove it.
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        // Set the latitude and longitude of this location
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Check if this is the very first location or not. If so, set the original location
        // variables to their respective value.
        int initial = 0;
        if (initial == 0) {
            orLatitude = latitude;
            orLongitude = longitude;
            orLatLng = latLng;
            initial++;
        }

        // Set all the marker options
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mOriginalMarker = latLng;
        markerOptionsCurrent = markerOptions;
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        // add aditional variables so that we actually zoom in to the right position,
        // without this it would zoom in too fast and go to the wrong spot.
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);

        // Display text to inform the user of their current location
        Toast.makeText(MapsActivity.this,"Your Current Location", Toast.LENGTH_LONG).show();

        // Stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // display some text to inform the user
        Toast.makeText(MapsActivity.this,"You do not have a working connection." +
                        " Please check your GPS and internet settings and make sure these are" +
                        " correct.",
                Toast.LENGTH_LONG).show();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission(){
        // Check if we have permission or not
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // clicking on a marker makes it draggable
        marker.setDraggable(true);
        // function to check if it was a double click
        if (doubleClick) {
            // if it was a double click, open the place in a browser
            // get the title of the place
            String placeName = marker.getTitle();
            // create an url which we will navigate to when pressing the marker
            String url = "http://www.google.com/search?q=" + placeName;
            // new intent for this action
            Intent i = new Intent(Intent.ACTION_VIEW);
            // set the data for the intent
            i.setData(Uri.parse(url));
            // start the activity
            startActivity(i);
        } else {
            // after one click, set the variable that registers double clicks to true
            this.doubleClick = true;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleClick = false;
                }
                // a double click can happen in a time span of 4 seconds
            }, 1000);
        }
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        // required empty method
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        // required empty method
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // set the end latitude and longitude
        endLatitude = marker.getPosition().latitude;
        endLongitude =  marker.getPosition().longitude;

        Log.d("end_lat",""+endLatitude);
        Log.d("end_lng",""+endLongitude);
    }
}



