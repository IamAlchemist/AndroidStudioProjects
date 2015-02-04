package com.tiny.wizard.samplebasic;
// Created by wizard on 2/4/15.

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class FilterReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra(context.getString(R.string.message));
        Toast.makeText(context, "received message : " + message, Toast.LENGTH_SHORT).show();
    }
}
