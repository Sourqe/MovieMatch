package nl.tue.moviematch;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment {

    private String genreFilter = ""; // String for the genre filter
    private String yearFilter = ""; // String for the year filter
    private String lengthFilter = ""; // String for the length filter
    private String ratingFilter = ""; // String for the rating filter
    private int movieId = 0; // Int containing the id of the movie
    private int currentPage = 1; // Int containing the id of the page
    private ArrayList similarMovieIds = new ArrayList(); // List of all ids of similar movies

    public MovieListFragment() {
        // Required empty public constructor
    }

    public void findMovieId(String query) {
        RequestQueue queue = Volley.newRequestQueue( getContext() );
        Log.d("query", query);
        // String containing the url which we will use to search for movies
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" +
                "73555db489b850892725e21fcd3c26ea" + "&language=en-US&include_adult=false&query="
                + query;

        JsonObjectRequest request = new JsonObjectRequest( Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse( JSONObject response ) {
                        try {
                            Log.d("Result: ", response.toString() );
                            Log.d("id", response.getJSONArray("results").
                                    getJSONObject( 0 ).getString( "id" ) );
                            movieId = Integer.parseInt(response.getJSONArray("results").
                                    getJSONObject( 0 ).getString( "id" ) );
                            // Find similar movies
                            findSimilar(movieId ,currentPage++);
                        } catch(JSONException e) {
                            Log.e("error", e.getMessage());
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.getMessage());
                    }
                }
        );
        // Add the request to the queue
        queue.add(request);
    }

    public void findSimilar(int id, int page) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.themoviedb.org/3/movie/" + String.valueOf(id) + "/similar" +
                "?api_key=" + "73555db489b850892725e21fcd3c26ea" + "&page=" +
                String.valueOf(page) + "&language=en-US";

        // Initiate array list which will contain all ids of similar movies
        ArrayList<Integer> similarIds = new ArrayList<Integer>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    // For each result in results
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movie = results.getJSONObject(i);
                        // Get the movie info of a said movie using its id
                        getMovieInfo(Integer.parseInt(movie.getString("id")),
                                i == results.length()-1 );
                    }
                } catch(JSONException e) {
                    Log.e("error", e.getMessage());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error ){
                        Log.e("error", error.toString() );
                    }
                }

        );
        // Add the request to the queue
        queue.add(request);
    }

    public void getMovieInfo(int movieId, final boolean lastNumber){
        RequestQueue queue = Volley.newRequestQueue( getContext());
        String url = "https://api.themoviedb.org/3/movie/" + String.valueOf(movieId) +
                "?api_key=" + "73555db489b850892725e21fcd3c26ea";

        JsonObjectRequest request = new JsonObjectRequest( Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // An array list of genres that will contain each genre
                    ArrayList genres = new ArrayList();
                    // An JSONArray of genre ids that will contain each genre id
                    JSONArray genre_ids = response.getJSONArray("genres");
                    // For each genre id
                    for (int j = 0; j < genre_ids.length(); j++){
                        // Adding the genre id to genres
                        genres.add(genre_ids.getJSONObject(j).get("id"));
                    }
                    // Get all the filter elements
                    String rating = response.getString("vote_average");
                    String length = response.getString("runtime");
                    String year = response.getString("release_date").substring(0,3);
                    TextView list = getView().findViewById( R.id.movieList);

                    // If we have filters active
                    if (checkFilter(genres, year, length, rating)) {
                        similarMovieIds.add(Integer.parseInt(response.getString("id")) );
                        Log.d("movieIds", similarMovieIds.toString());

                        //Bundle bundle = new Bundle();
                        //bundle.putString( "backdropPath", response.get("backdrop_path").toString());
                        //bundle.putString( "id", response.get("id").toString());
                        //bundle.putString( "title", response.get("original_title").toString());
                        // Append the list with the original titel as a string
                        list.append( response.get("original_title").toString() + "\n" );
                        } else if (lastNumber){
                            // If no movies found
                            if (list.getText().equals("")) {
                                // Display a toast to inform the user
                                Toast.makeText(getActivity(), "No results found. Try " +
                                                "adjusting your filters or searching for a " +
                                                "different movie!",
                                        Toast.LENGTH_SHORT).show();
                            }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error ){
                        Log.e("error", error.toString() );
                    }
                });
        // Add the request to the queue
        queue.add(request);
    }

    public boolean checkFilter(ArrayList genres, String year, String length, String rating){
        Log.d("genres", genres.toString());

        //Check genreFilter
        if( !this.genreFilter.equals("ALL") ) {
            Log.d("genrefilter setting", this.genreFilter);
            int reqGenreId = 0;

            // Switch case statement mapping an id to the requested genre id
            switch (this.genreFilter) {
                case "HORROR": //id 27
                    reqGenreId = 27;
                    break;
                case "DRAMA": //id 18
                    reqGenreId = 18;
                    break;
                case "ROMANCE": //id 10749
                    reqGenreId = 10749;
                    break;
                case "ACTION": // id 28
                    reqGenreId = 28;
                    break;
                case "COMEDY": // id 35
                    reqGenreId = 35;
                    break;
            }
            // If we do not have the genre
            if (!genres.contains(reqGenreId)) {
                return false;
            }
        }

        // Check yearFilter
        if (!this.yearFilter.equals("ALL")) {
            if (!year.substring(0,2).equals( this.yearFilter.substring(0,2))){
                Log.d("filter","year does not match");
                return false;
            }
        }

        // Check lengthFilter
        if (!this.lengthFilter.equals("ALL")) {
            // Switch case statement checking the length filter
            switch (this.lengthFilter) {
                case "< 60 min":
                    if (Integer.parseInt(length) >= 60 ) {
                        Log.d("filter","length does not match");
                        return false;
                    }
                    break;
                case "< 120 min":
                    if (Integer.parseInt(length) >= 120){
                        Log.d("filter","length does not match");
                        return false;
                    }
                    break;
                case "< 180 min":
                    if (Integer.parseInt(length) >= 180) {
                        Log.d("filter","length does not match");
                        return false;
                    }
                    break;
                case "> 180 min":
                    if (Integer.parseInt(length) <= 180) {
                        Log.d("filter","length does not match");
                        return false;
                    }
                    break;
            }
        }

        //Check ratingFilter
        if (!this.ratingFilter.equals("ALL")) {
            if (Integer.parseInt(this.ratingFilter) >= Integer.parseInt(rating.substring(0,1))) {
                Log.d("filter","rating does not match");
                return false;
            }
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Create view
        View v = inflater.inflate(R.layout.fragment_movie_list, container, false);

        // Get the textview from the layout
        TextView list = v.findViewById(R.id.movieList);
        list.clearComposingText();
        // Initiate String query with the query
        String query = getArguments().getString("query");
        // Initiate all filters with respective arguments
        genreFilter = getArguments().getString("genreFilter");
        yearFilter = getArguments().getString("yearFilter");
        lengthFilter = getArguments().getString("lengthFilter");
        ratingFilter = getArguments().getString("ratingFilter");
        // Find the movie id of the query
        findMovieId(query);
        // Return view
        return v;
    }
}