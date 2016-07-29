package com.morgenworks.daggerdemo;

import javax.inject.Singleton;

import dagger.Component;

/**
 * This is Created by wizard on 7/29/16.
 */

@Singleton
@Component(modules = {VehicleModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
}
