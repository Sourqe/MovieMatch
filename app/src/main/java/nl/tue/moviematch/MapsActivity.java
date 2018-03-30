package nl.tue.moviematch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener
{

    // Declare global variables
    private GoogleMap mMap; // the google map that is being used
    GoogleApiClient mGoogleApiClient; // the google api client
    private Location mLastLocation; // the most recent location
    private Marker mCurrLocationMarker; // the marker of the current location
    private LatLng mOriginalMarker; // the location of the original current location marker
    private LocationRequest mLocationRequest; // the location of the request
    private int PROXIMITY_RADIUS = 10000; // the max. radius to use when looking for places
    private double latitude, longitude; // the latitude and longitude of a location
    private double end_latitude, end_longitude; // the new latitude and longitude of a location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the content of the activity
        setContentView(R.layout.activity_maps);
        // if our version is high enough, ask permission first
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services is available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        } else {
            Log.d("onCreate","Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private boolean CheckGooglePlayServices() {
        // set the googleAPI
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        // set if the the play services are available or not
        int result = googleAPI.isGooglePlayServicesAvailable(this);

        // if the play services are not available
        if (result != ConnectionResult.SUCCESS) {
            // deal with the error
            if (googleAPI.isUserResolvableError(result)) {
                // get an error dialog
                googleAPI.getErrorDialog(this, result,0).show();
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
        // set the Map
        mMap = googleMap;

        // initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // if we have permission
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // build the API client
                buildGoogleApiClient();
                // set the location as enabled
                mMap.setMyLocationEnabled(true);
            }
        } else { // if our version is low enough, don't ask permission first
            // build the API client
            buildGoogleApiClient();
            // set the location as enabled
            mMap.setMyLocationEnabled(true);
        }
        // set an onMarkerDragListener
        mMap.setOnMarkerDragListener(this);
        // set an onMarkerClickListener
        mMap.setOnMarkerClickListener(this);
    }

    // synchronized to deal with multiple threads
    protected synchronized void buildGoogleApiClient() {
        // build the API client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        // connect the client
        mGoogleApiClient.connect();
    }

    public void onClick(View v) {
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        // check which button gets pressed
        switch(v.getId()) {
            // the search button gets pressed
            case R.id.B_search: {
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
                    // set new geocoder as a Geocoder type
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        // set the addresslist with a maximum of 5 result
                        // use the location as String name (check documentation)
                        addressList = geocoder.getFromLocationName(location, 5);
                    } catch (IOException e) { // catch if necessary
                        e.printStackTrace();
                    }
                    // if the addressList is not null
                    if (addressList != null) {
                        // for each address in the list
                        for (int i = 0; i < addressList.size(); i++) {
                            // get the address for the original place
                            Address myAddress = addressList.get(i);
                            // get the latLng of the location
                            LatLng latLng = new LatLng(myAddress.getLatitude(),
                                    myAddress.getLongitude());
                            // set the location of the marker to the latLng
                            markerOptions.position(latLng);
                            // move the camera to that place
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            // add a marker for that location
                            mMap.addMarker(markerOptions);
                            // get the first location
                            Address locationSearch = addressList.get(0);
                            // set the longitude of that first location
                            longitude = locationSearch.getLongitude();
                            // set the latitude of that first location
                            latitude = locationSearch.getLatitude();

                            // set the dataTransfer
                            dataTransfer = new Object[2];
                            // we are going to search for the movie_theater places
                            String theater = "movie_theater";
                            // get the url, using the latitude, longitude, and places type
                            String url = getUrl(latitude, longitude, theater);
                            // get the nearby places data (see other file)
                            getNearbyPlacesData = new GetNearbyPlacesData();
                            // first object is the map
                            dataTransfer[0] = mMap;
                            // second object is the url
                            dataTransfer[1] = url;

                            getNearbyPlacesData.execute(dataTransfer);
                            Toast.makeText(MapsActivity.this, "Showing Movie " +
                                    "Theaters near: "+location, Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    // repeat the same process as above
                    dataTransfer = new Object[2];
                    String theater = "movie_theater";
                    String url = getUrl(latitude, longitude, theater);
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
                    // if the user has moved the current position marker,
                    // then we can execute the data transfer
                    // basically, the same process as above but now for navigating
                    Log.d("location", "entered if statement");
                    dataTransfer = new Object[3];
                    String url = getDirectionsUrl();
                    GetDirectionsData getDirectionsData = new GetDirectionsData();
                    // store the polylines in a list to remove them after
                    List<Polyline> polylines = getDirectionsData.getPolylines();
                    // remove all polylines from the map
                    for (Polyline polyline : polylines) {
                        // remove a polyline
                        polyline.remove();
                    }
                    // clear the list
                    polylines.clear();
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;
                    dataTransfer[2] = new LatLng(end_latitude, end_longitude);
                    getDirectionsData.execute(dataTransfer);
                    break;
                }
        }
    }

    private String getDirectionsUrl() {
        // retrieve the url for the directions
        // the initial link
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        // the origin of the path
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
        // the destination of the path
        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        // the key for the API
        googleDirectionsUrl.append("&key="+"AIzaSyC7wipWxG58EF-zTsBIYji_ue7hM1W9mBc");
        // return the link as a string
        return googleDirectionsUrl.toString();
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        // retrieve the url for nearby places
        // the initial link
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        // the location of the place
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        // the max radius of those places
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        // the type of the nearby place
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        // the key for the API
        googlePlacesUrl.append("&key=" + "AIzaSyDHnqcmCGkC_rfWSL_oVk39JbmmOjJ6VS8");
        Log.d("getUrl", googlePlacesUrl.toString());
        // return the link as a string
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnected(Bundle bundle) {
        // set the location request
        mLocationRequest = new LocationRequest();
        // set the intervals
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        // set the priority of the location request
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        // check if we have permission to the location
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // retrieve the location
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // necessary empty method
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");
        // set the last location as the location
        mLastLocation = location;
        // if the last location is not null
        if (mCurrLocationMarker != null) {
            // remove the location
            mCurrLocationMarker.remove();
        }

        // set the latitude and longitude
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        // set the latLng using the latitude and longitude of the location
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        // set all the marker options
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        // add the marker to the map
        mOriginalMarker = latLng;
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        // display some text to inform the user
        Toast.makeText(MapsActivity.this,"Your Current Location",
                Toast.LENGTH_LONG).show();

        // stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // necessary empty method
    }

    // set a request code
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission(){
        // check if we have permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                // prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            // if we have no permission
            return false;
        } else {
            // if we have permission
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // if request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    // do the contacts-related task you need to do
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        // if we do not have a client yet, build one
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        // set the location enabled to true
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request can be
            // placed here. We will only use the GPS in this app though, so we do not need it.
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // clicking on a marker makes it draggable
        marker.setDraggable(true);
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // when done dragging a marker, set end lat and long
        end_latitude = marker.getPosition().latitude;
        end_longitude =  marker.getPosition().longitude;

        Log.d("end_lat",""+end_latitude);
        Log.d("end_lng",""+end_longitude);
    }
}


