package nl.tue.moviematch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.support.v4.app.FragmentManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class MovieSearchFragment extends Fragment {

    public MovieSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create new view
        View v = inflater.inflate(R.layout.fragment_movie_search, container, false);

        // String arrays containing the dropdown menu items
        String[] SPINNERLISTGENRE = {"HORROR", "DRAMA", "ROMANCE", "ACTION", "COMEDY", "ALL"};
        String[] SPINNERLISTYEAR = {"1990s", "1980s", "1970s", "2000s", "2010s", "ALL"};
        String[] SPINNERLISTLENGTH = {"< 60 min", "< 120 min", "< 180 min", "> 180 min", "ALL"};
        String[] SPINNERLISTRATING = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "ALL"};

        // One dropdown menu
        // Initialize spinner and connect it with spinner id
        final MaterialBetterSpinner betterSpinnerGenre = (MaterialBetterSpinner) v.findViewById(R.id.android_material_design_spinner_genre);
        // Initialize arrayAdapter and put the elements of the string arrays in their respective array
        ArrayAdapter<String> adapterGenre = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLISTGENRE);
        // Dropdown box with one item per line
        adapterGenre.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // Connect both
        betterSpinnerGenre.setAdapter(adapterGenre);

        final MaterialBetterSpinner betterSpinnerYear = (MaterialBetterSpinner) v.findViewById(R.id.android_material_design_spinner_year);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLISTYEAR);
        adapterYear.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        betterSpinnerYear.setAdapter(adapterYear);

        final MaterialBetterSpinner betterSpinnerLength = (MaterialBetterSpinner) v.findViewById(R.id.android_material_design_spinner_length);
        ArrayAdapter<String> adapterLength = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLISTLENGTH);
        adapterLength.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        betterSpinnerLength.setAdapter(adapterLength);

        final MaterialBetterSpinner betterSpinnerRating = (MaterialBetterSpinner) v.findViewById(R.id.android_material_design_spinner_rating);
        ArrayAdapter<String> adapterRating = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLISTRATING);
        adapterRating.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        betterSpinnerRating.setAdapter(adapterRating);

        final EditText searchField = (EditText) v.findViewById( R.id.search_for_movie );
        Button button = (Button) v.findViewById(R.id.search_movie);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MovieListFragment movieListFragment = new MovieListFragment();

                Bundle bundle = new Bundle();
                bundle.putString("query", searchField.getText().toString());

                if( betterSpinnerGenre.getText().toString().equals("") ){
                    bundle.putString("genreFilter", "ALL");
                } else {
                    bundle.putString("genreFilter", betterSpinnerGenre.getText().toString());
                }

                if( betterSpinnerYear.getText().toString().equals("")){
                    bundle.putString("yearFilter" , "ALL");
                } else {
                    bundle.putString("yearFilter", betterSpinnerYear.getText().toString());
                }

                if(  betterSpinnerRating.getText().toString().equals("") ){
                    bundle.putString("ratingFilter", "ALL");
                } else {
                    bundle.putString("ratingFilter", betterSpinnerRating.getText().toString());
                }

                if( betterSpinnerLength.getText().toString().equals("") ){
                    bundle.putString("lengthFilter", "ALL");
                } else {
                    bundle.putString("lengthFilter", betterSpinnerLength.getText().toString());
                }

                movieListFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.movieListFragmentContainer, movieListFragment);
                fragmentTransaction.commit();
            }
        });

        // Return the view
        return v;
    }
}