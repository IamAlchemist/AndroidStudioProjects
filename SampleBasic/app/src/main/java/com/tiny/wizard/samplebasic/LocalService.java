package com.tiny.wizard.samplebasic;
// Created by wizard on 2/3/15.

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class LocalService extends Service {
    private final IBinder binder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        LocalService getService(){
            return LocalService.this;
        }
    }

    public int getRandomNumber(){
        return Util.randomNumber(100);
    }
}
