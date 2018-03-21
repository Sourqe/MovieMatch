package nl.tue.moviematch;

import android.content.Context;
import android.os.Bundle;
<<<<<<< HEAD
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.support.v4.app.FragmentManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
=======
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
>>>>>>> origin/home-activity
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

=======
import android.widget.Button;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;
import android.view.View.OnClickListener;

>>>>>>> origin/home-activity
public class TheaterSearchFragment extends Fragment {
    OnSearchClickListener mCallback;

<<<<<<< HEAD
=======
    // Container Activity must implement this interface
    public interface OnSearchClickListener {
        void passData(String name);
    }
>>>>>>> origin/home-activity
    public TheaterSearchFragment() {
        // Required empty public constructor
    }

<<<<<<< HEAD
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
=======
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try
        {
            mCallback = (TheaterSearchFragment.OnSearchClickListener) context;
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
        View v = inflater.inflate(R.layout.fragment_theater_search, container, false);
        final EditText tName = (EditText) v.findViewById(R.id.search_movie3);
        final Button button = (Button) v.findViewById(R.id.search_theater);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                TheaterListFragment theaterListFragment = new TheaterListFragment();
                fragmentTransaction.replace(R.id.theaterListFragmentContainer, theaterListFragment);
                fragmentTransaction.commit();

                 mCallback.passData(tName.getText().toString());
            }
        });
        return v;
    }


>>>>>>> origin/home-activity
}
