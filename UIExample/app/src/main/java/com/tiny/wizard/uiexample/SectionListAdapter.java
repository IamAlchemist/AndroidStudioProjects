package com.tiny.wizard.uiexample;
// Created by wizard on 12/17/14.

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class SectionListAdapter<T> extends BaseAdapter {

    public abstract int numberOfRowsInSection(int section);
    public abstract int numberOfSections();

    public abstract int resourceIdForRowInSection(int row, int section);
    public abstract int resourceIdForSection(int row);

    public int heightForRowInSection(int row, int section){
        return 44;
    }
    public int heightForHeaderInSection(int section) {
        return 0;
    }
    public int heightForFooterInSection(int section){
        return 0;
    }

    public String titleForHeaderInSection(int section){
        return "";
    }
    public String titleForFooterInSection(int section){
        return "";
    }




    ArrayList<T> data = new ArrayList<T>();
    ArrayList<String> sections = new ArrayList<String>();

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
