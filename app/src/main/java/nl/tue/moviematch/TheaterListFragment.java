package nl.tue.moviematch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.support.v4.app.FragmentManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TheaterListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TheaterListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;
<<<<<<< HEAD

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class TheaterListFragment extends Fragment {

=======

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class TheaterListFragment extends Fragment {
    final static String DATA_RECEIVE = "data_receive";
    TextView showReceivedData;
>>>>>>> origin/home-activity

    public TheaterListFragment() {
        // Required empty public constructor
    }

<<<<<<< HEAD

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_list, container, false);

        return v;
    }
=======
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_theater_list, container, false);
        showReceivedData = (TextView) v.findViewById(R.id.showReceivedData);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            showReceivedData.setText(args.getString(DATA_RECEIVE));
        }
    }
>>>>>>> origin/home-activity
}
