package nl.tue.moviematch;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;


public class HomeActivity extends MainActivity implements MovieSearchFragment.DataPassListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.movieSearchFragmentContainer) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            MovieSearchFragment msFragment = new MovieSearchFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movieSearchFragmentContainer, msFragment).commit();
        }
    }

    @Override
    public void passData(String data) {
        MovieListFragment fragmentB = new MovieListFragment ();
        Bundle args = new Bundle();
        args.putString(MovieListFragment.DATA_RECEIVE, data);
        fragmentB .setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movieListFragmentContainer, fragmentB )
                .commit();
    }
}

