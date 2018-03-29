package nl.tue.moviematch;

import android.graphics.Color;
import android.os.AsyncTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import java.io.IOException;

public class GetDirectionsData extends AsyncTask<Object,String,String> {
    // declare global variables
    GoogleMap mMap; // create a googlemap
    String url; // create a string url
    String googleDirectionsData; // create data for the directions
    String duration, distance; // create duration and distance
    LatLng latLng; // create a LatLng

    @Override
    protected String doInBackground(Object... objects) {
        // set the GoogleMap
        mMap = (GoogleMap)objects[0];
        // set the url
        url = (String)objects[1];
        // set the latLng
        latLng = (LatLng)objects[2];

        // create a new downloadurl
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            // set the google direction data to the url
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) { // catch if necessary
            e.printStackTrace();
        }
        // return the data for directions
        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {
        // create new list with directions
        String[] directionsList;
        // create new parser of a dataparser type
        DataParser parser = new DataParser();
        // set the directionsList
        directionsList = parser.parseDirections(s);
        // display the direction
        displayDirection(directionsList);
    }

    public void displayDirection(String[] directionsList) {
        // set the count to the length of the directions list
        int count = directionsList.length;
        // for the whole directions list
        for (int i = 0; i <count ;i++) {
            // create new options as a polylineoptions type
            PolylineOptions options = new PolylineOptions();
            // set the color of the line to red
            options.color(Color.RED);
            // set the width of the line to 10
            options.width(10);
            // add all the directions
            options.addAll(PolyUtil.decode(directionsList[i]));
            // put it all in the map
            mMap.addPolyline(options);
        }
    }
}