package com.tiny.wizard.samplebasic;
// Created by wizard on 2/3/15.

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

public class AIDLBindServiceActivity extends Activity {

    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_service);

        textView = (TextView)findViewById(R.id.bind_service_text);
    }

    @Override
    protected void onStart(){
        super.onStart();

        bindService(new Intent(IRemoteService.class.getName()), serviceConnection,
                Context.BIND_AUTO_CREATE);
        bindService(new Intent(IRemoteServiceSecond.class.getName()), serviceConnectionSecond,
                Context.BIND_AUTO_CREATE);

        isBound = true;
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(isBound){
            isBound = false;
            if(service != null){
                try {
                    service.unregisterCallback(callback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            unbindService(serviceConnection);
            unbindService(serviceConnectionSecond);
        }
    }

    public void buttonClicked(View view) {
        TextView textView = (TextView)findViewById(R.id.bind_service_pid_text);
        int pid = 0;

        if(serviceSecond != null){
            try {
                pid = serviceSecond.getPid();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        textView.setText("Remote Process Id : " + pid);
    }

    private boolean isBound = false;
    private IRemoteService service;
    private IRemoteServiceSecond serviceSecond;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            service = IRemoteService.Stub.asInterface(iBinder);
            try {
                service.registerCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            isBound = false;
        }
    };

    private IRemoteServiceCallback callback = new IRemoteServiceCallback.Stub(){
        @Override
        public void valueChanged(int value) throws RemoteException {
            final int showValue = value;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("Value Changed : " + showValue);
                }
            });
        }
    };

    private ServiceConnection serviceConnectionSecond = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serviceSecond = IRemoteServiceSecond.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceSecond = null;
        }
    };

}
