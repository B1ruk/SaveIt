package io.start.biruk.saveit;

import android.app.Application;

import io.start.biruk.saveit.di.component.DaggerMainComponent;
import io.start.biruk.saveit.di.component.MainComponent;
import io.start.biruk.saveit.di.module.MainModule;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by biruk on 5/22/2018.
 */
public class App extends Application {

    private static MainComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Bazer.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        appComponent= DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .build();
    }

    public static MainComponent getAppComponent(){
        return appComponent;
    }
}
