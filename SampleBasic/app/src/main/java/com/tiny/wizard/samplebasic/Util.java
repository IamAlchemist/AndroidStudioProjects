package com.tiny.wizard.samplebasic;
// Created by wizard on 1/30/15.

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Random;

public class Util {

    public static final int REQUEST_IMAGE_GET = 1;
    public static int randomNumber(int range){
        return random.nextInt(range);
    }

    public static void showNotification(Context context, int stringId,
                                        NotificationManager notificationManager) {
        CharSequence text = context.getText(stringId);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, NotificationActivity.class), 0);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .setContentTitle(context.getText(R.string.notification_sample_activity))
                .setTicker(text)
                .build();

        notificationManager.notify(stringId, notification);
    }

    private static Random random = new Random();
}
