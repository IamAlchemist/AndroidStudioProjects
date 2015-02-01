package com.tiny.wizard.samplebasic;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new SimpleAdapter(this, getData(),
                android.R.layout.simple_list_item_1, new String[] {"title"},
                new int[] { android.R.id.text1 }));
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> myData = new ArrayList<>();

        Intent intentWithCustomAction = new Intent();
        intentWithCustomAction.setAction(getResources().getString(R.string.sample_basic_action));

        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intentWithCustomAction, 0);

        for(ResolveInfo resolveInfo : resolveInfos){
            CharSequence labelSequence = resolveInfo.loadLabel(pm);
            String label = labelSequence == null ?
                    resolveInfo.activityInfo.name : labelSequence.toString();

            Map<String, Object> entries = new HashMap<>();
            Intent intent = new Intent();
            intent.setClassName(resolveInfo.activityInfo.applicationInfo.packageName,
                    resolveInfo.activityInfo.name);
            entries.put("title", label);
            entries.put("intent", intent);

            myData.add(entries);
        }

        return myData;
    }

    private void startActivity(Class<? extends Activity> activityClass){
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    public void showImplicitIntent(View view) {
        startActivity(ImplicitIntentActivity.class);
    }

    public void showActivityWithFlag(View view) {
        startActivity(TaskFlagActivity.class);
    }
}
