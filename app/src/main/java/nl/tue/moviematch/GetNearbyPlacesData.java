package nl.tue.moviematch;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {
    // declare global variables
    String googlePlacesData; // create new string for the places data
    GoogleMap mMap; // declare new map
    String url; // declare new string url

    @Override
    protected String doInBackground(Object... objects) {
        // set the GoogleMap
        mMap = (GoogleMap)objects[0];
        // set the url
        url = (String)objects[1];
        // create a new downloadurl as a downloadurl type
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            // set the data of the google places
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (IOException e) { // catch if necessary
            e.printStackTrace();
        }
        // return the data of the places
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        // set the nearbyPlacesList to null
        List<HashMap<String, String>> nearbyPlaceList = null;
        // parser is a DataParser type
        DataParser parser = new DataParser();
        // set the nearbyPlaceList with all its content
        nearbyPlaceList = parser.parse(s);
        // show the nearby places of the nearbyPlaceList
        showNearbyPlaces(nearbyPlaceList);
    }

    private void showNearbyPlaces(List<HashMap<String,String>> nearbyPlaceList) {
        // for the all elements in the nearbyPlaceList
        for (int i = 0; i < nearbyPlaceList.size(); i++) {
            // set markerOptions as a MakerOptions type
            MarkerOptions markerOptions = new MarkerOptions();
            // get element i from the nearbyPlaceList and put it in googlePlace
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);
            Log.d("onPostExecute", "Entered into showing locations");

            // set the name of the place
            String placeName = googlePlace.get("place_name");
            // set the vicinity of the location
            String vicinity = googlePlace.get("vicinity");
            // set the latitude
            double lat = Double.parseDouble(googlePlace.get("lat"));
            // set the longitude
            double lng = Double.parseDouble(googlePlace.get("lng"));

            // set latLng as a LatLng type with the latitude and the longitude
            LatLng latLng = new LatLng(lat, lng);
            // set all the marker related things
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker
                    (BitmapDescriptorFactory.HUE_BLUE));
            // add the marker to the map with its options
            mMap.addMarker(markerOptions);
        }


    }
}
