package com.morgenworks.daggerdemo;

import javax.inject.Inject;

/**
 * This is Created by wizard on 7/29/16.
 */
public class Vehicle {
    private Motor motor;

    @Inject
    public Vehicle(Motor motor){
        this.motor = motor;
    }

    public void increaseSpeed(int value){
        motor.accelerate(value);
    }

    public void stop(){
        motor.brake();
    }

    public int getSpeed(){
        return motor.getRpm();
    }
}
