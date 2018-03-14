package nl.tue.moviematch;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;


public class HomeActivity extends AppCompatActivity implements MovieSearchFragment.DataPassListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MovieSearchFragment movieSearchFragment = new MovieSearchFragment();
        fragmentTransaction.add(R.id.movieSearchFragmentContainer, movieSearchFragment);
        fragmentTransaction.commit();
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
