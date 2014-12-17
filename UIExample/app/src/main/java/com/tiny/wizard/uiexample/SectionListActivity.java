package com.tiny.wizard.uiexample;
// Created by wizard on 12/17/14.

import android.app.ListActivity;
import android.os.Bundle;

public class SectionListActivity extends ListActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_list);
    }
}
