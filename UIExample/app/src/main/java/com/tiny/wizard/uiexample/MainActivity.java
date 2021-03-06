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

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.text_view);
        textView.setText(com.tiny.wizard.viewlibrary.Util.utilTest());

        configUI();
    }

    private void configUI() {
        Button button;

        button = (Button)findViewById(R.id.btn_popup_activity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(PopMenuActivity.class);
            }
        });

        button = (Button)findViewById(R.id.btn_popup_window);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(PopWindowActivity.class);
            }
        });

        button = (Button)findViewById(R.id.btn_section_list);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SectionListActivity.class);
            }
        });

        button = (Button)findViewById(R.id.btn_custom_section_list);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(CustomSectionActivity.class);
            }
        });
    }

    public void startActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
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

    public void showCircleBar(View view) {
        startActivity(CircleBarActivity.class);
    }

    public void showNavTab(View view) {
        startActivity(NavTabActivity.class);
    }

    public void showWheelIndicator(View view) {
        startActivity(WheelIndicatorActivity.class);
    }
}
