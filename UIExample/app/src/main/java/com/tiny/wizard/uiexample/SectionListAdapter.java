package com.tiny.wizard.uiexample;

// Created by wizard on 12/17/14.
// Support single type of cell view and single type of header - v0.1

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class SectionListAdapter extends BaseAdapter {
    private Context context;

    public SectionListAdapter(Context context){
        this.context = context;
    }

    public final int VIEW_TYPE_CELL = 0;
    public final int VIEW_TYPE_HEADER = 1;

    private class InternalIndexPath{
        IndexPath indexPath;
        int type;
        InternalIndexPath(){
            indexPath = new IndexPath();
            type = VIEW_TYPE_CELL;
        }
    }

    public class IndexPath{
        int row;
        int section;
    }

    public abstract int numberOfRowsInSection(int section);
    public abstract int numberOfSections();
    public abstract View getViewForIndexPath(IndexPath indexPath, View view, ViewGroup viewGroup);

    @Override
    public int getViewTypeCount(){
        return 2;
    }

    @Override
    public int getItemViewType(int position){
        InternalIndexPath internalIndexPath = indexPathFromPosition(position);
        return internalIndexPath.type;
    }

    public int heightForRow(IndexPath indexPath){
        return 44;
    }
    public int heightForHeaderInSection(int section) {
        return 44;
    }
    public int heightForFooterInSection(int section){
        return 44;
    }

    public String titleForHeaderInSection(int section){
        return "";
    }
    public String titleForFooterInSection(int section){
        return "";
    }

    public View getViewForSectionHeader(int section, View view, ViewGroup viewGroup){
        return null;
    }

    @Override
    public int getCount() {
        int allSize = 0;
        int numberOfSections = numberOfSections();
        for(int i = 0; i < numberOfSections; ++i){
            allSize += numberOfRowsInSection(i);
        }

        if(numberOfSections > 1){
            allSize += numberOfSections;
        }

        return allSize;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        InternalIndexPath internalIndexPath = indexPathFromPosition(i);

        View resultView = view;
        switch (internalIndexPath.type){
            case VIEW_TYPE_CELL:
                resultView = getViewForIndexPath(internalIndexPath.indexPath, view, viewGroup);
                break;
            case VIEW_TYPE_HEADER:
                resultView = viewForSectionHeader(internalIndexPath.indexPath.section, view, viewGroup);
                break;
        }

        return resultView;
    }

    private View viewForSectionHeader(int section, View view, ViewGroup viewGroup) {
        View headerView = getViewForSectionHeader(section, view, viewGroup);
        if(headerView != null)
            return headerView;

        if(view != null)
            headerView = view;
        else
            headerView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, viewGroup, false);

        TextView textView = (TextView)headerView.findViewById(android.R.id.text1);
        textView.setText(titleForHeaderInSection(section));

        return headerView;
    }

    private InternalIndexPath indexPathFromPosition(int position){
        InternalIndexPath internalIndexPath = new InternalIndexPath();
        int numberOfSections = numberOfSections();

        if(numberOfSections == 1){
            internalIndexPath.indexPath.row = position;
            internalIndexPath.indexPath.section = 0;
            internalIndexPath.type = VIEW_TYPE_CELL;
        }
        else{
            for(int sectionIndex = 0; sectionIndex < numberOfSections; ++sectionIndex){
                int numberOfRowsInSection = numberOfRowsInSection(sectionIndex);

                if(position < numberOfRowsInSection + 1){
                    internalIndexPath.indexPath.section = sectionIndex;
                    internalIndexPath.indexPath.row = position - 1;
                    internalIndexPath.type = getTypeOfIndexPath(internalIndexPath.indexPath);
            }
                else{
                    position -= numberOfRowsInSection + 1;
                }
            }
        }

        return internalIndexPath;
    }

    private int getTypeOfIndexPath(IndexPath indexPath) {
        if(indexPath.row == -1)
            return VIEW_TYPE_HEADER;
        return VIEW_TYPE_CELL;
    }


}
