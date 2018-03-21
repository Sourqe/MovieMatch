package nl.tue.moviematch;

import android.support.v4.app.FragmentManager;
<<<<<<< HEAD
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

=======
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
>>>>>>> origin/home-activity


public class HomeActivity extends MainActivity implements MovieSearchFragment.DataPassListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        setContentView(R.layout.activity_home);
=======
>>>>>>> origin/home-activity
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MovieSearchFragment movieSearchFragment = new MovieSearchFragment();
        fragmentTransaction.add(R.id.movieSearchFragmentContainer, movieSearchFragment);
        fragmentTransaction.commit();
<<<<<<< HEAD
=======
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
>>>>>>> origin/home-activity
    }
}

