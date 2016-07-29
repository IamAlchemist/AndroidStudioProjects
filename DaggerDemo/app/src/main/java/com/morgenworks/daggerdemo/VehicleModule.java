package com.morgenworks.daggerdemo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This is Created by wizard on 7/29/16.
 */

@Module
public class VehicleModule {
    @Singleton
    @Provides
    Motor provideMotor() {
        return new Motor();
    }

    @Singleton
    @Provides
    Vehicle provideVehicle() {
        return new Vehicle(new Motor());
    }
}
