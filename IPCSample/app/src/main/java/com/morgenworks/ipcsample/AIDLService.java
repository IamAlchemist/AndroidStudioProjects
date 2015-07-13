package com.morgenworks.ipcsample;
// Created by wizard on 7/12/15.

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.support.annotation.Nullable;

public class AIDLService extends Service {

    private final IRemoteService.Stub binder = new IRemoteService.Stub(){

        @Override
        public int getPid() throws RemoteException {
            return android.os.Process.myPid();
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            return;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
