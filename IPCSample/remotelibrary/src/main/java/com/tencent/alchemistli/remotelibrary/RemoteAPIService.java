package com.tencent.alchemistli.remotelibrary;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * Created by alchemistli on 3/31/15.
 */
public class RemoteAPIService extends Service {
    private Student stuInfo;

    private IRemoteAPI.Stub binder = new IRemoteAPI.Stub() {
        @Override
        public Student getName() throws RemoteException {
            stuInfo.name = stuInfo.name.toUpperCase();
            return stuInfo;
        }

        @Override
        public void setName(Student st) throws RemoteException {
            stuInfo = st;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
