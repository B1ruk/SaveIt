package io.start.biruk.saveit.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.start.biruk.saveit.R;

/**
 * Created by biruk on 16/07/18.
 */

public class BaseThemeActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme();
    }


    protected void setTheme() {
        String themeKey = getResources().getString(R.string.theme_key);
        String theme = preferences.getString(themeKey, "Light");

        switch (theme) {
            case "Dark":
                setTheme(R.style.AppThemeDark);
                break;
            case "Light":
                setTheme(R.style.AppTheme);
                break;
        }
    }

}
