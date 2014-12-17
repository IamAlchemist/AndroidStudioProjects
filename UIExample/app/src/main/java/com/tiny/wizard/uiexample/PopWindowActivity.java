package com.tiny.wizard.uiexample;
// Created by wizard on 12/16/14.

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

public class PopWindowActivity extends Activity {
    private Button button;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window);

        button = (Button)findViewById(R.id.activity_popup_window_click_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(PopWindowActivity.this);
                View v  = layoutInflater.inflate(R.layout.popup_window, null);
                final PopupWindow popupWindow = new PopupWindow(v, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(button, 0, 0);

            }
        });
    }

}
