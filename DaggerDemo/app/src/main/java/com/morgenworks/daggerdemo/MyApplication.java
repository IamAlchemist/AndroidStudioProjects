package com.morgenworks.daggerdemo;

import android.app.Application;

/**
 * This is Created by wizard on 7/29/16.
 */
public class MyApplication extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().vehicleModule(new VehicleModule()).build();
    }

    public static void inject(MainActivity target) {
        component.inject(target);
    }
}
