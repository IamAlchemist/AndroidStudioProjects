package com.tiny.wizard.samplebasic;
// Created by wizard on 2/3/15.

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

public class RemoteService extends Service {
    private static final int REPORT_MSG = 1;

    int value = 0;

    final RemoteCallbackList<IRemoteServiceCallback> callbacks = new RemoteCallbackList<>();
    private NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        if(IRemoteService.class.getName().equals(intent.getAction()))
            return binder;
        if(IRemoteServiceSecond.class.getName().equals(intent.getAction()))
            return secondBinder;
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Util.showNotification(this, R.string.remote_service_started, notificationManager);
        handler.sendEmptyMessage(REPORT_MSG);
    }

    @Override
    public void onDestroy(){
        notificationManager.cancel(R.string.remote_service_started);
        super.onDestroy();
    }

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case REPORT_MSG:{
                    int value = ++RemoteService.this.value;

                    final int N = callbacks.beginBroadcast();
                    for (int i = 0; i < N; ++i){
                        try {
                            callbacks.getBroadcastItem(i).valueChanged(value);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    callbacks.finishBroadcast();

                    sendMessageDelayed(obtainMessage(REPORT_MSG), 2*1000);
                }break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private final IRemoteService.Stub binder = new IRemoteService.Stub() {
        @Override
        public void registerCallback(IRemoteServiceCallback callback) throws RemoteException {
            if(callback != null)
                callbacks.register(callback);
        }

        @Override
        public void unregisterCallback(IRemoteServiceCallback callback) throws RemoteException {
            if(callback != null)
                callbacks.unregister(callback);
        }
    };

    private final IRemoteServiceSecond.Stub secondBinder = new IRemoteServiceSecond.Stub() {
        @Override
        public int getPid() throws RemoteException {
            return Process.myPid();
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                               double aDouble, String aString) throws RemoteException {
        }
    };
}
