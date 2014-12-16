package com.tiny.wizard.uiexample;
// Created by wizard on 12/16/14.

import android.content.Context;
import android.widget.Toast;

public class Util {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
