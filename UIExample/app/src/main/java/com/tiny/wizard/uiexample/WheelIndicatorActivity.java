package com.tiny.wizard.uiexample;
// Created by wizard on 1/15/15.

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class WheelIndicatorActivity extends Activity{

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_indicator);

        imageView = (ImageView)findViewById(R.id.spinner_indicator);

        RotateAnimation ra = new RotateAnimation(0.0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        ra.setInterpolator(linearInterpolator);
        ra.setDuration(1000);
        ra.setRepeatCount(Animation.INFINITE);
        ra.setZAdjustment(Animation.ZORDER_TOP);
        ra.setFillAfter(true);

        imageView.startAnimation(ra);
    }

    @Override
    public void onPause(){
        super.onPause();
        imageView.clearAnimation();
    }
}
