package nl.tue.moviematch;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class TheaterActivity extends MainActivity implements TheaterSearchFragment.OnSearchClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MovieSearchFragment movieSearchFragment = new MovieSearchFragment();
        fragmentTransaction.add(R.id.theaterSearchFragmentContainer, movieSearchFragment);
        fragmentTransaction.commit();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.theaterSearchFragmentContainer) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            TheaterSearchFragment tsFragment = new TheaterSearchFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.theaterSearchFragmentContainer, tsFragment).commit();
        }
    }
    @Override
    public void passData(String name) {
        TheaterListFragment tlFragment = new TheaterListFragment();
        Bundle args = new Bundle();
        args.putString(TheaterListFragment.DATA_RECEIVE, name);
        tlFragment .setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.theaterListFragmentContainer, tlFragment )
                .commit();
    }
}
