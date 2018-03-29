package nl.tue.moviematch;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class DataParser {
    private HashMap<String,String> getDuration(JSONArray googleDirectionsJson) {
        // create global variables
        HashMap<String,String> googleDirectionsMap = new HashMap<>(); // hash map storing directions
        String duration = ""; // storing the duration
        String distance =""; // storing the distance

        try {
            // set the duration
            duration = googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("text");
            // set the distance
            distance = googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("text");
            // put the duration and distance in the map
            googleDirectionsMap.put("duration" , duration);
            googleDirectionsMap.put("distance", distance);

        } catch (JSONException e) { // catch exception if we cannot do the above
            e.printStackTrace();
        }
        // return the directions
        return googleDirectionsMap;
    }

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson)
    {
        // create local variables
        HashMap<String, String> googlePlacesMap = new HashMap<>(); // map for the places
        String placeName = "-NA-"; // the place name
        String vicinity = "-NA-"; // the vicinity
        String latitude = ""; // the latitude of the location
        String longitude = ""; // the longitude of the location
        String reference = ""; // the reference
        Log.d("getPlace", "Entered");

        try {
            if (!googlePlaceJson.isNull("name")) {
                // set the placename if we have a placename
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                // set the vicinity if we have a vicinity
                vicinity = googlePlaceJson.getString("vicinity");
            }
            // retrieve the latitude and longitude of a location
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            // retrieve the reference
            reference = googlePlaceJson.getString("reference");
            // put all the above retrieved information in the map
            googlePlacesMap.put("place_name" , placeName);
            googlePlacesMap.put("vicinity" , vicinity);
            googlePlacesMap.put("lat" , latitude);
            googlePlacesMap.put("lng" , longitude);
            googlePlacesMap.put("reference" , reference);

            Log.d("getPlace", "Putting Places");

        } catch (JSONException e) { // catch exception if we cannot do the actions above
            e.printStackTrace();
        }
        // return the places
        return googlePlacesMap;
    }

    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray) {
        // declare local variables
        int count = jsonArray.length(); // count the array length
        List<HashMap<String,String>> placesList = new ArrayList<>(); // arraylist for the places
        HashMap<String,String> placeMap = null; // the placemap is initially null
        Log.d("Places", "getPlaces");

        // for the array length
        for (int i = 0; i < count; i++) {
            try {
                // initiate the place map and add it to the places list
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) { // if we cannot do the actions above
                e.printStackTrace();
            }
        }
        // return the list of places
        return placesList;
    }

    public List<HashMap<String,String>> parse(String jsonData) {
        // create local variables
        JSONArray jsonArray = null; // array is initially null
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse");
            // initiate object
            jsonObject = new JSONObject(jsonData);
            // set the array
            jsonArray = jsonObject.getJSONArray("results");

        } catch (JSONException e) { // if we cannot do the actions above
            e.printStackTrace();
        }
        // return the list of places
        return getPlaces(jsonArray);
    }

    public String[] parseDirections(String jsonData) {
        // create local variables
        JSONArray jsonArray = null; //array is initially null
        JSONObject jsonObject;

        try {
            // initiate object
            jsonObject = new JSONObject(jsonData);
            // set the array
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        } catch (JSONException e) { // if we cannot do the actions above
            e.printStackTrace();
        }
        // return all directions
        return getPaths(jsonArray);
    }

    public String[] getPaths(JSONArray googleStepsJson ) {
        // declare local variables
        int count = googleStepsJson.length(); // set count to the length of the steps
        String[] polylines = new String[count];

        // for the whole length of the JSONArray
        for (int i = 0; i <count; i++) {
            try {
                // set the path
                polylines[i] = getPath(googleStepsJson.getJSONObject(i));
            } catch (JSONException e) { // if we cannot do the actions above
                e.printStackTrace();
            }
        }
        // return the polylines
        return polylines;
    }

    public String getPath(JSONObject googlePathJson) {
        // declare empty polyline String
        String polyline = "";
        try {
            // try to set it to its path
            polyline = googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) { // if we cannot do the actions above
            e.printStackTrace();
        }
        // return the polyline
        return polyline;
    }
}