package com.tiny.wizard.samplebasic;
// Created by wizard on 2/4/15.

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LoaderCustomActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        FragmentManager fm = getFragmentManager();
        if(fm.findFragmentById(android.R.id.content) == null){
            CursorCustomListFragment listFragment = new CursorCustomListFragment();
            fm.beginTransaction().add(android.R.id.content, listFragment).commit();
        }
    }

    public static class InterestingConfigChanges {
        final Configuration lastConfiguration = new Configuration();
        int lastDensity;

        boolean applyNewConfig(Resources resources){
            int configChanges = lastConfiguration.updateFrom(resources.getConfiguration());
            boolean densityChanged = lastDensity != resources.getDisplayMetrics().densityDpi;

            if(densityChanged || (configChanges & (ActivityInfo.CONFIG_LOCALE
                    | ActivityInfo.CONFIG_UI_MODE | ActivityInfo.CONFIG_SCREEN_LAYOUT)) != 0){
                lastDensity = resources.getDisplayMetrics().densityDpi;
                return true;
            }

            return false;
        }
    }

    public static class PackageIntentReceiver extends BroadcastReceiver{
        final AppListLoader loader;

        public PackageIntentReceiver(AppListLoader loader){
            this.loader = loader;

            IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
            filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
            filter.addDataScheme("package");

            loader.getContext().registerReceiver(this, filter);

            IntentFilter sdFilter = new IntentFilter();
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
            loader.getContext().registerReceiver(this, sdFilter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            loader.onContentChanged();
        }
    }

    public static class AppEntry{
        private final File apkFile;
        private final AppListLoader loader;
        private final ApplicationInfo appInfo;
        public String label;
        private Drawable icon;
        private boolean mounted ;

        public AppEntry(AppListLoader loader, ApplicationInfo info){
            this.loader = loader;
            this.appInfo = info;
            apkFile = new File(info.sourceDir);
        }

        public Drawable getIcon(){
            if(icon == null){
                if(apkFile.exists()){
                    icon = appInfo.loadIcon(loader.packageManager);
                    return icon;
                }
                else
                    mounted = false;
            }
            else if( !mounted ){
                if(apkFile.exists()){
                    mounted = true;
                    icon = appInfo.loadIcon(loader.packageManager);
                    return icon;
                }
            }
            else
                return icon;

            return loader.getContext().getResources().getDrawable(
                    android.R.drawable.sym_def_app_icon);
        }

        @Override
        public String toString(){
            return label;
        }

        public void loadLabel(Context context){
            if(label == null || !mounted){
                if(!apkFile.exists()){
                    mounted = false;
                    label = appInfo.packageName;
                }
                else {
                    mounted = true;
                    CharSequence label = appInfo.loadLabel(context.getPackageManager());
                    this.label = label != null ? label.toString() : appInfo.packageName;
                }
            }
        }
    }

    public static class AppListLoader extends AsyncTaskLoader<List<AppEntry>>{
        final InterestingConfigChanges lastConfig = new InterestingConfigChanges();
        final PackageManager packageManager;

        List<AppEntry> apps;
        PackageIntentReceiver packageObserver;

        public AppListLoader(Context context) {
            super(context);
            packageManager = getContext().getPackageManager();
        }

        @Override
        public List<AppEntry> loadInBackground() {
            List<ApplicationInfo> appInfos = packageManager.getInstalledApplications(
                    PackageManager.GET_UNINSTALLED_PACKAGES |
                    PackageManager.GET_DISABLED_COMPONENTS );

            appInfos = appInfos == null ? new ArrayList<ApplicationInfo>() : appInfos;

            final Context context = getContext();

            List<AppEntry> entries = new ArrayList<>(appInfos.size());
            for(ApplicationInfo info : appInfos){
                AppEntry entry = new AppEntry(this, info);
                entry.loadLabel(context);
                entries.add(entry);
            }

            Collections.sort(entries, ALPHA_COMPARATOR);

            return entries;
        }

        @Override
        public void deliverResult(List<AppEntry> apps){
            if(isReset()){
                if(apps != null){
                    onReleaseResources(apps);
                }
            }

            List<AppEntry> oldApps = this.apps;
            this.apps = apps;

            if(isStarted()){
                super.deliverResult(this.apps);
            }

            if(oldApps != null){
                onReleaseResources(oldApps);
            }
        }

        @Override
        protected void onStartLoading(){
            super.onStartLoading();
            if(apps != null)
                deliverResult(apps);

            if(packageObserver == null)
                packageObserver = new PackageIntentReceiver(this);

            boolean configChanged = lastConfig.applyNewConfig(getContext().getResources());
            if(takeContentChanged() || apps == null || configChanged)
                forceLoad();
        }

        @Override
        protected void onStopLoading(){
            super.onStopLoading();
            cancelLoad();
        }

        @Override
        public void onCanceled(List<AppEntry> apps){
            super.onCanceled(apps);
            onReleaseResources(apps);
        }

        @Override
        protected void onReset(){
            super.onReset();
            onStartLoading();

            if(apps != null){
                onReleaseResources(apps);
            }

            if(packageObserver != null){
                getContext().unregisterReceiver(packageObserver);
                packageObserver = null;
            }
        }

        @SuppressWarnings("UnusedParameters")
        protected void onReleaseResources(List<AppEntry> apps) {
        }
    }

    public static class CursorCustomListFragment extends ListFragment
            implements SearchView.OnCloseListener,
            SearchView.OnQueryTextListener,
            LoaderManager.LoaderCallbacks<List<AppEntry>>
    {
        AppListAdapter listAdapter;
        SearchView searchView;
        String currentFilter;

        @Override
        public void onActivityCreated(Bundle saveInstanceState){
            super.onActivityCreated(saveInstanceState);

            setEmptyText("No application");
            setHasOptionsMenu(true);

            listAdapter = new AppListAdapter(getActivity());
            setListAdapter(listAdapter);

            setListShown(true);

            getLoaderManager().initLoader(0, null, this);
        }

        public static class MySearchView extends SearchView{
            public MySearchView(Context context){ super(context); }

            @Override
            public void onActionViewCollapsed(){
                setQuery("", false);
                super.onActionViewCollapsed();
            }
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
            MenuItem item  = menu.add("Search");
            item.setIcon(android.R.drawable.ic_menu_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                            | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

            searchView = new MySearchView(getActivity());
            searchView.setOnCloseListener(this);
            searchView.setOnQueryTextListener(this);
            searchView.setIconifiedByDefault(true);

            item.setActionView(searchView);
        }

        @Override
        public Loader<List<AppEntry>> onCreateLoader(int i, Bundle bundle) {
            return new AppListLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<List<AppEntry>> listLoader, List<AppEntry> appEntries) {
            listAdapter.setData(appEntries);
            if(isResumed())
                setListShown(true);
            else
                setListShownNoAnimation(true);
        }

        @Override
        public void onLoaderReset(Loader<List<AppEntry>> listLoader) {
            listAdapter.setData(null);
        }

        @Override
        public boolean onClose() {
            if(!TextUtils.isEmpty(searchView.getQuery()))
                searchView.setQuery(null, true);
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String s) {
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            currentFilter = !TextUtils.isEmpty(newText) ? newText : null;
            listAdapter.getFilter().filter(currentFilter);
            return true;
        }
    }

    private static final Comparator<AppEntry> ALPHA_COMPARATOR = new Comparator<AppEntry>() {
        private final Collator collator = Collator.getInstance();
        @Override
        public int compare(AppEntry appEntry, AppEntry appEntry2) {
            return collator.compare(appEntry.label, appEntry2.label);
        }
    };

    public static class AppListAdapter extends ArrayAdapter<AppEntry>{
        private final LayoutInflater layoutInflater;

        public AppListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
            layoutInflater = LayoutInflater.from(context);
        }

        public void setData(List<AppEntry> data){
            clear();
            if(data != null)
                addAll(data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView == null)
                convertView = layoutInflater.inflate(R.layout.list_item_icon_text, parent, false);

            AppEntry appEntry = getItem(position);
            ((ImageView)convertView.findViewById(R.id.list_item_icon)).setImageDrawable(appEntry.getIcon());
            ((TextView)convertView.findViewById(R.id.list_item_text)).setText(appEntry.label);

            return convertView;
        }
    }
}
