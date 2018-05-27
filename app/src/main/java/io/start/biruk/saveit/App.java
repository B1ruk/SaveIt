package io.start.biruk.saveit;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by biruk on 5/22/2018.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Bazer.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
