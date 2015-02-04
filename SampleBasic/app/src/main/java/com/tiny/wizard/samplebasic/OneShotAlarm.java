package com.tiny.wizard.samplebasic;
// Created by wizard on 2/4/15.

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class OneShotAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, R.string.one_shot_alarm_gone, Toast.LENGTH_SHORT).show();
    }
}
