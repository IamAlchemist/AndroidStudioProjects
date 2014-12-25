package com.tiny.wizard.threadexample;
// Created by wizard on 12/25/14.

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskExampleActivity extends Activity{
    Timer timer;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_task);
    }

    @Override
    protected void onResume(){
        super.onResume();

        startTimer();
    }

    @Override
    protected void onPause(){
        super.onPause();

        stopTimer();
    }

    private void startTimer() {
        timer = new Timer();

        initializeTimerTask();

        timer.schedule(timerTask, 0, 10000);
    }

    private void initializeTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                TimerTaskExampleActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
                        final String strDate = simpleDateFormat.format(calendar.getTime());

                        Toast.makeText(getApplicationContext(), strDate, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
    }

    public void stopTimerTask(View view) {
        stopTimer();
    }

    private void stopTimer() {
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

}
