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

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class TheaterSearchFragment extends Fragment {

    public TheaterSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create new view
        View v = inflater.inflate(R.layout.fragment_movie_search, container, false);

        Button button = (Button) v.findViewById(R.id.search_theater);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                TheaterListFragment theaterListFragment = new TheaterListFragment();
                fragmentTransaction.replace(R.id.theaterListFragmentContainer, theaterListFragment);
                fragmentTransaction.commit();
            }
        });
        // Return the view
        return v;
    }
}
