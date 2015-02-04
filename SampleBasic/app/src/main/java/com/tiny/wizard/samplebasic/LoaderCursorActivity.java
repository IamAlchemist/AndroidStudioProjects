package com.tiny.wizard.samplebasic;
// Created by wizard on 2/4/15.

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import static android.provider.ContactsContract.Contacts;

public class LoaderCursorActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        FragmentManager fm = getFragmentManager();
        if(fm.findFragmentById(android.R.id.content) == null){
            CursorLoadListFragment listFragment = new CursorLoadListFragment();
            fm.beginTransaction().add(android.R.id.content, listFragment).commit();
        }
    }

    public static class CursorLoadListFragment extends ListFragment
            implements SearchView.OnQueryTextListener,
            SearchView.OnCloseListener,
            LoaderManager.LoaderCallbacks<Cursor>
    {
        SimpleCursorAdapter adapter;
        private MySearchView searchView;
        private String currentFilter;

        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);

            setEmptyText("No phone numbers");

            setHasOptionsMenu(true);

            adapter = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_list_item_2,
                    null,
                    new String[] { Contacts.DISPLAY_NAME, Contacts.CONTACT_STATUS},
                    new int[] {android.R.id.text1, android.R.id.text2},
                    0
                    );

            setListAdapter(adapter);

            setListShown(false);

            getLoaderManager().initLoader(0, null, this);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
            MenuItem item = menu.add("Search");
            item.setIcon(android.R.drawable.ic_menu_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

            searchView = new MySearchView(getActivity());
            searchView.setOnQueryTextListener(this);
            searchView.setOnCloseListener(this);
            searchView.setIconifiedByDefault(true);

            item.setActionView(searchView);
        }

        static final String[] CONTACTS_SUMMARY_PROJECTION = new String[]{
                Contacts._ID,
                Contacts.DISPLAY_NAME,
                Contacts.CONTACT_STATUS,
                Contacts.CONTACT_PRESENCE,
                Contacts.PHOTO_ID,
                Contacts.LOOKUP_KEY
        };

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            Uri baseUri;
            if(currentFilter != null)
                baseUri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI,
                        Uri.encode(currentFilter));
            else
                baseUri = Contacts.CONTENT_URI;

            String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                    + Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                    + Contacts.DISPLAY_NAME + " != '' ))";

            return new CursorLoader(getActivity(), baseUri, CONTACTS_SUMMARY_PROJECTION,
                    select, null, Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            adapter.swapCursor(cursor);

            if(isResumed())
                setListShown(true);
            else
                setListShownNoAnimation(true);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            adapter.swapCursor(null);
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
            String newFilter = !TextUtils.isEmpty(newText) ? newText : null;

            if(currentFilter == null && newFilter == null)
                return true;

            if(currentFilter != null && currentFilter.equals(newFilter))
                return true;

            currentFilter = newFilter;
            getLoaderManager().restartLoader(0, null, this);

            return true;
        }

        public static class MySearchView extends SearchView {
            public MySearchView(Context context) {
                super(context);
            }

            @Override
            public void onActionViewCollapsed(){
                setQuery("", false);
                super.onActionViewCollapsed();
            }
        }
    }
}
