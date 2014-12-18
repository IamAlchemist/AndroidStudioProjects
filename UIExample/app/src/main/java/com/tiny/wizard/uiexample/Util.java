package com.tiny.wizard.uiexample;
// Created by wizard on 12/16/14.

import android.content.Context;
import android.widget.Toast;

public class Util {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static int pixelFromDp(Context context, int dp){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp*scale + 0.5f);
    }
}
