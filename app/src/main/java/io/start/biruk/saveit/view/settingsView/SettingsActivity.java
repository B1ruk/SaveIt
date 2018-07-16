package io.start.biruk.saveit.view.settingsView;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import io.start.biruk.saveit.R;

import static android.content.SharedPreferences.*;

/**
 * Created by biruk on 16/07/18.
 */

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setSubtitle("Settings");

        addPreferencesFromResource(R.xml.pref_settings);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
