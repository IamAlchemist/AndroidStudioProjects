package com.tiny.wizard.samplebasic;
// Created by wizard on 1/30/15.

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TaskFlagActivity extends Activity{
    static int index = 0;

    int[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.BLACK};

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_flag);
        findViewById(R.id.activity_task_flag_content).setBackgroundColor(colors[index++ % colors.length]);
        int lastIndex = getIntent().getIntExtra("index", -1);
        ((TextView)findViewById(R.id.activity_task_flag_text)).setText("LAST INDEX : " + lastIndex);
    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        ((TextView)findViewById(R.id.activity_task_flag_text_new)).setText("NEW LAST INDEX : " + intent.getIntExtra("index", -2));
    }

    @Override
    public void onResume(){
        super.onResume();
        if(index%colors.length == 0){
            setContentView(R.layout.activity_task_flag_2);
        }
    }


    public void showActivityWithFlagNewTask(View view) {
        startActivityWithFlag(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    private void startActivityWithFlag(int flag) {
        Intent intent = new Intent(this, TaskFlagActivity.class);
        intent.addFlags(flag);
        intent.putExtra("index", index++);
        startActivity(intent);
    }

    public void showActivityWithFlagClearTop(View view) {
        startActivityWithFlag(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public void showActivityWithFlagSingleTop(View view) {
        startActivityWithFlag(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public void showActivityWithNoFlag(View view) {
        startActivityWithFlag(0);
    }
}
