package nl.tue.moviematch;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment {
    final static String DATA_RECEIVE = "data_receive";
    TextView showReceivedData;

    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_list, container, false);
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
}
