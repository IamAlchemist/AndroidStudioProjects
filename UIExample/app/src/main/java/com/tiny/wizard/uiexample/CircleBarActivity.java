package com.tiny.wizard.uiexample;
// Created by wizard on 12/26/14.

import android.app.Activity;
import android.os.Bundle;

public class CircleBarActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_circle_bar);

        CircularBar circularBar = (CircularBar)findViewById(R.id.circle_bar_circle);
        circularBar.setProgress(0.75f);
    }
}
