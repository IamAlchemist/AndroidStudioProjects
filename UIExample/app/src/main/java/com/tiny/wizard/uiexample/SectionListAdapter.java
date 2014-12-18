package com.tiny.wizard.uiexample;

// Created by wizard on 12/17/14.
// Support single type of cell view and single type of header - v0.1

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class SectionListAdapter extends BaseAdapter {
    protected final Context context;

    public SectionListAdapter(Context context){
        this.context = context;
    }

    private final int VIEW_TYPE_CELL = 0;
    private final int VIEW_TYPE_HEADER = 1;

    private class InternalIndexPath{
        public final IndexPath indexPath;
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

    @SuppressWarnings("CanBeFinal")
    public class Padding{
        public int left = 0;
        public int top = 0;
        public int right = 0;
        public int bottom = 0;
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


    @SuppressWarnings({"SameReturnValue", "WeakerAccess", "UnusedParameters"})
    protected View getViewForSectionHeader(int section, View view, ViewGroup viewGroup){
        return null;
    }
    @SuppressWarnings({"SameReturnValue", "WeakerAccess", "UnusedParameters"})
    protected Padding paddingForHeader(int section){
        return null;
    }
    @SuppressWarnings({"SameReturnValue", "WeakerAccess", "UnusedParameters"})
    protected ViewGroup.LayoutParams layoutParamsForHeader(int section){
        return null;
    }
    @SuppressWarnings("UnusedParameters")
    protected String titleForHeaderInSection(int section){
        return "";
    }
    @SuppressWarnings({"SameReturnValue", "WeakerAccess", "UnusedParameters"})
    protected int sizeOfHeaderText(int section) {
        return 12;
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
        else {
            headerView = LayoutInflater.from(context).inflate(R.layout.section_header, viewGroup, false);
        }

        ViewGroup.LayoutParams layoutParams = layoutParamsForHeader(section);
        if(layoutParams != null)
            headerView.setLayoutParams(layoutParams);

        TextView textView = (TextView)headerView.findViewById(R.id.section_list_section_header);
        Padding padding = paddingForHeader(section);
        if(padding != null){
            textView.setPadding(padding.left, padding.top, padding.right, padding.bottom);
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeOfHeaderText(section));
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
                    break;
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
