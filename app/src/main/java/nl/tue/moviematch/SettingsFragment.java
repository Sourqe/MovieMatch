package nl.tue.moviematch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set the view of this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        // Set the switch for the night mode
        final Switch nSwitch = (Switch) v.findViewById(R.id.night_mode_switch);

        // If the night mode is on, then the switch has to be checked
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            nSwitch.setChecked (true);
        }
        // If the switch is not null, listen to actions that might happen
        if (nSwitch != null) {
            nSwitch.setOnCheckedChangeListener(this);
        }
        return v;
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // Display message for user so they know what is happening
        Toast.makeText(getActivity(), "Changes will apply after switching screens",
                Toast.LENGTH_LONG).show();
        // If the switch is checked
        if (isChecked) {
            // Set the night mode on
            AppCompatDelegate
                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            // In all other cases, the night mode is off
            AppCompatDelegate
                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}

