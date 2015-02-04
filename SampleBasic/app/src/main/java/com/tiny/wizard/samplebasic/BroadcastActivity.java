package com.tiny.wizard.samplebasic;
// Created by wizard on 2/4/15.

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BroadcastActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
    }

    public void sendBroadcast(View view) {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.message), "Hello World!");
        intent.setAction(getString(R.string.sample_basic_action));

        sendBroadcast(intent);
    }
}
