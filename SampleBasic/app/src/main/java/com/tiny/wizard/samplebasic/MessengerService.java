package com.tiny.wizard.samplebasic;
// Created by wizard on 2/3/15.

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.ArrayList;

public class MessengerService extends Service {
    public static final int MSG_UNREGISTER_CLIENT = 1;
    public static final int MSG_REGISTER_CLIENT = 2;
    public static final int MSG_SAY_HELLO = 3;

    private NotificationManager notificationManager;

    ArrayList<Messenger> clients = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate(){
        super.onCreate();

        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Util.showNotification(this, R.string.messenger_service_started, notificationManager);
    }

    @Override
    public void onDestroy(){
        notificationManager.cancel(R.string.messenger_service_started);

        super.onDestroy();
    }


    final Messenger messenger = new Messenger(new IncomingHandler());

    class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MSG_REGISTER_CLIENT:
                    clients.add(msg.replyTo);
                    break;

                case MSG_UNREGISTER_CLIENT:
                    clients.remove(msg.replyTo);
                    break;

                case MSG_SAY_HELLO:
                    if(msg.replyTo != null){
                        try {
                            Message reply_msg = Message.obtain(null, MSG_SAY_HELLO, Util.randomNumber(100), 0);
                            msg.replyTo.send(reply_msg);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }
}
