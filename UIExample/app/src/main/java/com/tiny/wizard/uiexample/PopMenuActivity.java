package com.tiny.wizard.uiexample;
// Created by wizard on 12/16/14.

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

public class PopMenuActivity extends Activity implements PopupMenu.OnMenuItemClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(PopMenuActivity.this, view);
                popupMenu.setOnMenuItemClickListener(PopMenuActivity.this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_comedy:
                Util.showToast(this, "Comedy Clicked");
                return true;
            case R.id.item_movies:
                Util.showToast(this, "Movies Clicked");
                return true;
            case R.id.item_music:
                Util.showToast(this, "Music Clicked");
                return true;
            default:
                return false;
        }
    }
}
