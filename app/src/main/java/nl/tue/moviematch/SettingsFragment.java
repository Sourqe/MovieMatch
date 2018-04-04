package nl.tue.moviematch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener  {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        final Switch nSwitch = (Switch) v.findViewById(R.id.switch3);
         nSwitch.setChecked (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES);

        if (nSwitch != null) {
            nSwitch.setOnCheckedChangeListener(this);
        }
        return v;
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(getActivity(), "Changes will apply after switching screens",
                Toast.LENGTH_LONG).show();
        if (isChecked) {
            AppCompatDelegate
                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate
                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}

