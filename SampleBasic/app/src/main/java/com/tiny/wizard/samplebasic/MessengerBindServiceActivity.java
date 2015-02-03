package com.tiny.wizard.samplebasic;
// Created by wizard on 2/3/15.

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

public class MessengerBindServiceActivity extends Activity{
    private boolean isBound = false;
    private Messenger serviceMessenger;
    private Messenger clientMessenger = new Messenger(new IncomingHandler());

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_service);
    }

    @Override
    protected void onStart(){
        super.onStart();

        bindService(new Intent(this, MessengerService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected  void onStop(){
        super.onStop();

        if(isBound){
            if(serviceMessenger != null){
                try {
                    Message msg = Message.obtain(null, MessengerService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = clientMessenger;
                    serviceMessenger.send(msg);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            unbindService(serviceConnection);
            isBound = false;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serviceMessenger = new Messenger(iBinder);
            isBound = true;

            try {
                Message msg = Message.obtain(null, MessengerService.MSG_REGISTER_CLIENT);
                msg.replyTo = clientMessenger;
                serviceMessenger.send(msg);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceMessenger = null;
            isBound = false;
        }
    };

    public void buttonClicked(View view) {
        if(isBound && serviceMessenger != null){
            try {
                Message msg = Message.obtain(null, MessengerService.MSG_SAY_HELLO);
                msg.replyTo = clientMessenger;
                serviceMessenger.send(msg);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MessengerService.MSG_SAY_HELLO:
                    TextView textView = (TextView)findViewById(R.id.bind_service_text);
                    textView.setText("random Number : " + msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
