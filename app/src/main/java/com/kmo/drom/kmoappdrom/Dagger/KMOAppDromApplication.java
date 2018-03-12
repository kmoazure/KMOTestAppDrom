package com.kmo.drom.kmoappdrom.Dagger;

import android.app.Application;

/**
 * Created by kmoaz on 07.03.2018.
 */

public class KMOAppDromApplication extends Application {
    public static KMOAppDromApplication INSTANCE;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public AppComponent getAppComponent () {
        if (appComponent == null) {
            appComponent = DaggerAppComponent
                    .builder()
                    .build();
        }

        return appComponent;
    }
}
