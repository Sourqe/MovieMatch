package nl.tue.moviematch;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TheaterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MovieSearchFragment movieSearchFragment = new MovieSearchFragment();
        fragmentTransaction.add(R.id.theaterSearchFragmentContainer, movieSearchFragment);
        fragmentTransaction.commit();
    }
}
