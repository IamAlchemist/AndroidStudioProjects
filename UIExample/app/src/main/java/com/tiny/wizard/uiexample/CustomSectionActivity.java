package com.tiny.wizard.uiexample;
// Created by wizard on 12/18/14.

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomSectionActivity extends ListActivity {

    private ArrayList<ArrayList<String>> myData = new ArrayList<ArrayList<String>>();
    private ArrayList<String> sectionTitles = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_list);

        generateData();

        setListAdapter(new MyAdapter(this));
    }

    private void generateData() {
        for (int j = 0; j < 3; ++j){
            ArrayList<String> data = new ArrayList<String>();

            for(int i = 0; i < 20; ++i){
                data.add("item" + i);
            }

            sectionTitles.add("SECTION_" + j);
            myData.add(data);
        }
    }

    private class MyAdapter extends SectionListAdapter {
        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        public int numberOfRowsInSection(int section) {
            return myData.get(section).size();
        }

        @Override
        public int numberOfSections() {
            return myData.size();
        }

        @Override
        public View getViewForIndexPath(IndexPath indexPath, View view, ViewGroup viewGroup) {
            if(view == null){
                LayoutInflater inflater = LayoutInflater.from(CustomSectionActivity.this);
                view = inflater.inflate(R.layout.row_section_list, viewGroup, false);
            }

            TextView textView = (TextView)view.findViewById(R.id.row_section_list_text);
            textView.setText(myData.get(indexPath.section).get(indexPath.row));

            return view;
        }

        @Override
        protected View getViewForSectionHeader(int section, View view, ViewGroup viewGroup){
            if(view == null){
                view = LayoutInflater.from(context).inflate(R.layout.section_header, viewGroup, false);
            }

            TextView textView = (TextView)view.findViewById(R.id.section_list_section_header);
            textView.setText(sectionTitles.get(section));

            return view;
        }
    }
}
