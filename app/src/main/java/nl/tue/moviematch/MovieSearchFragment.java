package nl.tue.moviematch;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class MovieSearchFragment extends Fragment {

    DataPassListener mCallback;

    public interface DataPassListener{
        public void passData(String data);
    }

    public MovieSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try
        {
            mCallback = (DataPassListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+ " must implement OnImageClickListener");
        }
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
        final MaterialBetterSpinner betterSpinnerGenre = (MaterialBetterSpinner) v.findViewById(R.id.spinner_genre);
        // Initialize arrayAdapter and put the elements of the string arrays in their respective array
        ArrayAdapter<String> adapterGenre = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLISTGENRE);
        // Dropdown box with one item per line
        adapterGenre.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // Connect both
        betterSpinnerGenre.setAdapter(adapterGenre);

        final MaterialBetterSpinner betterSpinnerYear = (MaterialBetterSpinner) v.findViewById(R.id.spinner_year);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLISTYEAR);
        adapterYear.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        betterSpinnerYear.setAdapter(adapterYear);

        final MaterialBetterSpinner betterSpinnerLength = (MaterialBetterSpinner) v.findViewById(R.id.spinner_length);
        ArrayAdapter<String> adapterLength = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLISTLENGTH);
        adapterLength.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        betterSpinnerLength.setAdapter(adapterLength);

        final MaterialBetterSpinner betterSpinnerRating = (MaterialBetterSpinner) v.findViewById(R.id.spinner_rating);
        ArrayAdapter<String> adapterRating = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLISTRATING);
        adapterRating.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        betterSpinnerRating.setAdapter(adapterRating);

        final EditText title = (EditText) v.findViewById(R.id.search_for_movie);
        final Button button = (Button) v.findViewById(R.id.search_movie);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MovieListFragment movieListFragment = new MovieListFragment();
                fragmentTransaction.replace(R.id.movieListFragmentContainer, movieListFragment);
                fragmentTransaction.commit();

                String movieTitle = title.getText().toString();
                String genre = betterSpinnerGenre.getText().toString();
                String year = betterSpinnerYear.getText().toString();
                String length = betterSpinnerLength.getText().toString();
                String rating = betterSpinnerRating.getText().toString();
                String combined = movieTitle + genre + " " + year + " " + length + " " + rating;
                mCallback.passData(combined);
            }
        });

        // Return the view
        return v;
    }
}
