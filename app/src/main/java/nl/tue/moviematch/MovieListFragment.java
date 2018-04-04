package nl.tue.moviematch;

import nl.tue.moviematch.R;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment {

    private String genreFilter = "";
    private String yearFilter = "";
    private String lengthFilter = "";
    private String ratingFilter = "";
    private int movieId = 0;
    private int currentPage = 1;
    private ArrayList similarMovieIds = new ArrayList();


    public MovieListFragment() {
        // Required empty public constructor
    }

<<<<<<< HEAD
    public void findMovieId( String query ){

        RequestQueue queue = Volley.newRequestQueue( getContext() );
        Log.d("query", query);
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + getText(R.string.tmdbKey) + "&language=en-US&include_adult=false&query=" + query;

        JsonObjectRequest request = new JsonObjectRequest( Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

            @Override
            public void onResponse( JSONObject response ) {
                try{
                    Log.d("Result: ", response.toString() );
                    Log.d("id", response.getJSONArray("results").getJSONObject( 0 ).getString( "id" ) );
                    movieId = Integer.parseInt(response.getJSONArray("results").getJSONObject( 0 ).getString( "id" ) );

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

        queue.add(request);

    }

    public void findSimilar(int id, int page){

        RequestQueue queue = Volley.newRequestQueue( getContext() );
        String url = "https://api.themoviedb.org/3/movie/" + String.valueOf(id) + "/similar" + "?api_key=" + getText(R.string.tmdbKey) + "&page=" + String.valueOf(page) + "&language=en-US";
        ArrayList<Integer> similarIds = new ArrayList<Integer>();

        JsonObjectRequest request = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse( JSONObject response ) {
                try{

                    JSONArray results = response.getJSONArray("results");

                    for( int i = 0; i < results.length(); i++){

                        JSONObject movie = results.getJSONObject(i);
                        getMovieInfo( Integer.parseInt(movie.getString("id")) );

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

        queue.add(request);

    }

    public void getMovieInfo(int movieId){
        RequestQueue queue = Volley.newRequestQueue( getContext() );
        String url = "https://api.themoviedb.org/3/movie/" + String.valueOf(movieId) + "?api_key=" + getText(R.string.tmdbKey);

        JsonObjectRequest request = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    ArrayList genres = new ArrayList();
                    JSONArray genre_ids = response.getJSONArray("genres");
                    for( int j = 0; j < genre_ids.length(); j++){
                        genres.add( genre_ids.getJSONObject(j) );
                    }

                    String rating = response.getString("vote_average");
                    String length = response.getString("runtime");
                    String year = response.getString("release_date").substring(0,3);

                    if( checkFilter( genres, year, length, rating) ) {
                        similarMovieIds.add(Integer.parseInt(response.getString("id")) );
                        Log.d("movieIds", similarMovieIds.toString());

                        Bundle bundle = new Bundle();
                        bundle.putString( "backdropPath", response.get("backdrop_path").toString() );
                        bundle.putString( "id", response.get("id").toString() );
                        bundle.putString( "title", response.get("original_title").toString() );
                        //TODO call movieDataFragments

                        TextView list = getView().findViewById( R.id.movieList);
                        list.append( response.get("original_title").toString() + "\n" );
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

        queue.add(request);
    }

    public boolean checkFilter( ArrayList genres, String year, String length, String rating){
        Log.d("genres", genres.toString() );

        //Check genreFilter
        if( this.genreFilter != "ALL" ){
            int reqGenreId = 0;

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

            for( int j = 0; j < genres.size(); j++){
                if( genres.contains(reqGenreId)){
                    break;
                }
            }
            return false;
        }

        //Check yearFilter
        if( this.yearFilter != "ALL" ){
            if( !year.substring(0,2).equals( this.yearFilter.substring(0,2) ) ){
                return false;
            }
        }

        //Check lengthFilter
        if( this.lengthFilter != "ALL" ){
            switch (this.lengthFilter) {
                case "< 60 min":
                    if( Integer.parseInt(length) >= 60 ){
                        return false;
                    }
                    break;
                case "< 120 min":
                    if( Integer.parseInt(length) >= 120 ){
                        return false;
                    }
                    break;
                case "< 180 min":
                    if ( Integer.parseInt(length) >= 180 ){
                        return false;
                    }
                    break;
                case "> 180 min":
                    if( Integer.parseInt(length) <= 180 ){
                        return false;
                    }
                    break;
            }
        }

        //Check ratingFilter
        if( !this.ratingFilter.equals("ALL") ){
            if( Integer.parseInt(this.ratingFilter) >= Integer.parseInt(rating.substring(0,1)) ){
                return false;
            }
        }
        return true;
    }



=======
>>>>>>> home-activity
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_list, container, false);

        TextView list = v.findViewById( R.id.movieList);
        list.clearComposingText();

        String query = getArguments().getString("query");
        genreFilter = getArguments().getString("genreFilter");
        yearFilter = getArguments().getString("yearFilter");
        lengthFilter = getArguments().getString("lengthFilter");
        ratingFilter = getArguments().getString("ratingFilter");

        findMovieId(query);

        return v;
    }
}
