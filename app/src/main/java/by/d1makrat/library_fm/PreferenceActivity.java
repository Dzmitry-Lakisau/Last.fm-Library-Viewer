package by.d1makrat.library_fm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.preference.SeekBarPreference;
import android.view.Gravity;
import android.widget.SeekBar;
import android.preference.*;
import android.widget.TextView;
import android.widget.Toast;

//TODO write own preference window and Appsettings.getLimit to return int
public class PreferenceActivity extends android.preference.PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        EditTextPreference edit_Pref = (EditTextPreference) getPreferenceScreen().findPreference("limit");
        edit_Pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                try {
                    int temp = Integer.parseInt((String) newValue);
                    if (temp > 0 && temp < 1001) {
                        if (temp > 200) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Some Last.fm requests allow maximum limit equal to 200. Limit during this requests will be equal to 200", Toast.LENGTH_LONG);
                            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                            if (v != null) v.setGravity(Gravity.CENTER);
                            toast.show();
                        }
                        return true;
                    } else {
                        (Toast.makeText(getApplicationContext(), "Value must be between 1 and 1000 including", Toast.LENGTH_LONG)).show();
                        return false;
                    }
                } catch (NumberFormatException e) {
                    (Toast.makeText(getApplicationContext(), "Nonnumerical input", Toast.LENGTH_LONG)).show();
                    return false;
                }
            }
        });
    }
}
