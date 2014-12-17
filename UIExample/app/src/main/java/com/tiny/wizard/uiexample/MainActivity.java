package com.tiny.wizard.uiexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private Button popupActivityButton;
    private Button popupWindowButton;
    private Button sectionListButton;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        popupActivityButton = (Button)findViewById(R.id.btn_popup_activity);
        popupWindowButton = (Button)findViewById(R.id.btn_popup_window);
        sectionListButton = (Button)findViewById(R.id.btn_section_list);

        textView = (TextView)findViewById(R.id.text_view);
        textView.setText(com.tiny.wizard.viewlibrary.Util.utilTest());

        configUI();
    }

    private void configUI() {
        popupActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PopMenuActivity.class);
                startActivity(intent);
            }
        });

        popupWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PopWindowActivity.class);
                startActivity(intent);
            }
        });

        sectionListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SectionListActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
