package com.tiny.wizard.samplebasic;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ListActivity {

    private static final String LABEL_PREFIX = "com.tiny.wizard.samplebasic.main_activity.label_prefix";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String prefix = getIntent().getStringExtra(LABEL_PREFIX);

        setListAdapter(new SimpleAdapter(this, getData(prefix),
                android.R.layout.simple_list_item_1, new String[] {"title"},
                new int[] { android.R.id.text1 }));
    }

    private List<Map<String, Object>> getData(String prefix) {
        List<Map<String, Object>> myData = new ArrayList<>();

        Intent intentWithCustomAction = new Intent();
        intentWithCustomAction.setAction(getResources().getString(R.string.sample_basic_action));
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intentWithCustomAction, 0);

        HashMap<String, Boolean> nextLabelRecord = new HashMap<>();

        for(ResolveInfo resolveInfo : resolveInfos){
            CharSequence labelSequence = resolveInfo.loadLabel(pm);
            String label = labelSequence == null ?
                    resolveInfo.activityInfo.name : labelSequence.toString();
            prefix = prefix == null ? "" : prefix;
            if(prefix.length() == 0 || label.startsWith(prefix)) {
                String nextLabel = getNextLabel(prefix, label);
                Intent intent = makeIntent(prefix, label, resolveInfo);

                if(nextLabelRecord.get(nextLabel) == null){
                    nextLabelRecord.put(nextLabel, true);

                    Map<String, Object> entries = new HashMap<>();
                    entries.put("title", nextLabel);
                    entries.put("intent", intent);

                    myData.add(entries);
                }
            }
        }

        return myData;
    }

    private Intent makeIntent(String prefix, String label, ResolveInfo resolveInfo) {
        Intent intent = new Intent();

        String[] prefixes = prefix.split("/");
        String[] labels = label.split("/");

        if((prefix.length() == 0 && labels.length == 1) ||
                (prefix.length() != 0 && prefixes.length == labels.length - 1)){
            intent.setClassName(resolveInfo.activityInfo.applicationInfo.packageName,
                    resolveInfo.activityInfo.name);
        }
        else{
            prefix = prefix.length() == 0 ? labels[0] : prefix + labels[prefixes.length];

            intent.putExtra(LABEL_PREFIX, prefix + "/");
            intent.setClass(this, MainActivity.class);
        }

        return intent;
    }

    private String getNextLabel(String prefix, String label) {
        String[] prefixes = prefix.split("/");
        String[] labels = label.split("/");

        if(prefix.length() == 0)
            return labels[0];

        return labels[prefixes.length];
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

    @SuppressWarnings("unchecked")
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        Map<String, Object> map = (Map<String, Object>)l.getItemAtPosition(position);
        Intent intent = (Intent)map.get("intent");
        startActivity(intent);
    }
}
