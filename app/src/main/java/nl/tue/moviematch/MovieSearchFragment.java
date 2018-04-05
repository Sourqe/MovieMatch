package nl.tue.moviematch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class MovieSearchFragment extends Fragment {

    Button button; // the button for searching
    MaterialBetterSpinner betterSpinnerGenre; // the dropdown box for the genre
    MaterialBetterSpinner betterSpinnerYear; // the dropdown box for the year
    MaterialBetterSpinner betterSpinnerLength; // the dropdown box for the length
    MaterialBetterSpinner betterSpinnerRating; // the dropdown box for the rating
    EditText searchField; // the dropdown box for the search field

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
        betterSpinnerGenre = (MaterialBetterSpinner) v.findViewById(R.id.android_material_design_spinner_genre);
        // Initialize arrayAdapter and put the elements of the string arrays in their respective array
        ArrayAdapter<String> adapterGenre = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLISTGENRE);
        // Dropdown box with one item per line
        adapterGenre.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // Connect both
        betterSpinnerGenre.setAdapter(adapterGenre);

        betterSpinnerYear = (MaterialBetterSpinner) v.findViewById(R.id.android_material_design_spinner_year);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLISTYEAR);
        adapterYear.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        betterSpinnerYear.setAdapter(adapterYear);

        betterSpinnerLength = (MaterialBetterSpinner) v.findViewById(R.id.android_material_design_spinner_length);
        ArrayAdapter<String> adapterLength = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLISTLENGTH);
        adapterLength.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        betterSpinnerLength.setAdapter(adapterLength);

        betterSpinnerRating = (MaterialBetterSpinner) v.findViewById(R.id.android_material_design_spinner_rating);
        ArrayAdapter<String> adapterRating = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLISTRATING);
        adapterRating.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        betterSpinnerRating.setAdapter(adapterRating);

        // Set the layout components in the code
        searchField = (EditText) v.findViewById( R.id.search_for_movie );
        button = (Button) v.findViewById(R.id.search_movie);

        // If we do not have a network available
        if (!isNetworkAvailable()) {
            // Display an error dialog to the user
            showNetworkDisabledAlertToUser();
            // Set the button to disabled
            button.setEnabled(false);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String searchFieldText = searchField.getText().toString();
                Log.d("button", "pressed button");
                // Check if the search field is empty or not
                if (searchFieldText.matches("")) {
                    // display some text to inform the user
                    Toast.makeText(getActivity(), "You have to fill in at least some text in" +
                                    " order to search for similar movies!",
                            Toast.LENGTH_LONG).show();
                } else {// If the search field is not empty
                    Log.d("button", "searchfield is not empty");

                    // Call new fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    MovieListFragment movieListFragment = new MovieListFragment();

                    // Create bundle
                    Bundle bundle = new Bundle();
                    bundle.putString("query", searchField.getText().toString());

                    // Check for reach spinner if something has been filled in
                    if (betterSpinnerGenre.getText().toString().equals("")) {
                        bundle.putString("genreFilter", "ALL");
                    } else {
                        bundle.putString("genreFilter", betterSpinnerGenre.getText().toString());
                    }

                    if (betterSpinnerYear.getText().toString().equals("")) {
                        bundle.putString("yearFilter", "ALL");
                    } else {
                        bundle.putString("yearFilter", betterSpinnerYear.getText().toString());
                    }

                    if (betterSpinnerRating.getText().toString().equals("")) {
                        bundle.putString("ratingFilter", "ALL");
                    } else {
                        bundle.putString("ratingFilter", betterSpinnerRating.getText().toString());
                    }

                    if (betterSpinnerLength.getText().toString().equals("")) {
                        bundle.putString("lengthFilter", "ALL");
                    } else {
                        bundle.putString("lengthFilter", betterSpinnerLength.getText().toString());
                    }

                    movieListFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.movieListFragmentContainer, movieListFragment);
                    fragmentTransaction.commit();
                }
            }
        });

        // Return the view
        return v;
    }

    private void showNetworkDisabledAlertToUser() {
        // show the user that their GPS is disabled with a dialog interface
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("No network has been found on your device. Would you like " +
                "to search for a network?")
                .setCancelable(false)
                .setPositiveButton("Go to your Settings Page to search for a network",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_WIFI_SETTINGS);
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

    private boolean isNetworkAvailable() {
        // check if network is available
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}