package com.tiny.wizard.samplebasic;
// Created by wizard on 1/30/15.

import java.util.Random;

public class Util {

    public static final int REQUEST_IMAGE_GET = 1;
    public static int randomNumber(int range){
        return random.nextInt(range);
    }

    private static Random random = new Random();
}
