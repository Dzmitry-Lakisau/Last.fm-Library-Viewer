package by.d1makrat.library_fm.ui.activity;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.ui.CenteredToast;

import static by.d1makrat.library_fm.Constants.SCROBBLES_PER_PAGE_KEY;

//TODO write own preference window and Appsettings.getLimit to return int
public class PreferenceActivity extends android.preference.PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        EditTextPreference edit_Pref = (EditTextPreference) getPreferenceScreen().findPreference(SCROBBLES_PER_PAGE_KEY);
        edit_Pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                try {
                    int temp = Integer.parseInt((String) newValue);
                    if (temp > 0 && temp < 1001) {
                        if (temp > 200) {
                            CenteredToast.show(getApplicationContext(), "Some Last.fm requests allow maximum limit equal to 200. Limit during this requests will be equal to 200", Toast.LENGTH_SHORT);
                        }
                        AppContext.getInstance().setLimit(String.valueOf(temp));
                        return true;
                    } else {
                        CenteredToast.show(getApplicationContext(), "Value must be between 1 and 1000 including", Toast.LENGTH_SHORT);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    CenteredToast.show(getApplicationContext(), "Nonnumerical input", Toast.LENGTH_SHORT);
                    return false;
                }
            }
        });
    }
}