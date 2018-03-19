package nl.tue.moviematch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TheaterSearchFragment extends Fragment {
    OnSearchClickListener mCallback;

    // Container Activity must implement this interface
    public interface OnSearchClickListener {
        void SearchClicked();
    }
    public TheaterSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create new view
        View v = inflater.inflate(R.layout.fragment_theater_search, container, false);

        Button button = (Button) v.findViewById(R.id.search_theater);
        // Return the view
        return v;
    }

    public void onSearchClicked() {
        // Send the event to the host activity
        mCallback.SearchClicked();
    }
}
