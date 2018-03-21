package nl.tue.moviematch;


import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
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

    public MovieListFragment() {
        // Required empty public constructor
    }

    public void findMovieId( String query ){

        RequestQueue queue = Volley.newRequestQueue( getContext() );
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + R.string.tmdbKey + "&language=en-US&include_adult=true&query=" + query;

        JsonObjectRequest request = new JsonObjectRequest( Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

            @Override
            public void onResponse( JSONObject response ) {
                try{
                    movieId = response.getJSONObject("results").getInt("id");

                    findSimilar(movieId ,currentPage++);


                } catch(JSONException e) {
                    //TODO handle exception
                }
            }

        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //TODO handle error
                }
            }
        );

    }

    public void findSimilar(int id, int page){

        RequestQueue queue = Volley.newRequestQueue( getContext() );
        String url = "https://api.themoviedb.org/3/movie/" + String.valueOf(id) + "/similar" + "?api_key=" + R.string.tmdbKey + "&page=" + String.valueOf(page) + "&language=en-US";
        ArrayList<Integer> similarIds = new ArrayList<Integer>();

        JsonObjectRequest request = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse( JSONObject response ) {
                try{

                    JSONArray results = response.getJSONArray("results");
                    ArrayList<Integer> ids = new ArrayList<Integer>();

                    for( int i = 0; i < results.length(); i++){
                        if( checkFilter() ) {
                            ids.add(Integer.parseInt(results.getJSONArray(i).getString(4)));
                        }
                    }

                    //TODO call movieDataFragments

                } catch(JSONException e) {
                    //TODO handle exception
                }
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error ){
                    //TODO handle error
                }
            }

        );

    }

    public boolean checkFilter(){
        return false;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_list, container, false);

        String query = getArguments().getString("query");
        genreFilter = getArguments().getString("genreFilter");
        yearFilter = getArguments().getString("yearFilter");
        lengthFilter = getArguments().getString("lengthFilter");
        ratingFilter = getArguments().getString("ratingFilter");

        findMovieId(query);

        return v;
    }
}
