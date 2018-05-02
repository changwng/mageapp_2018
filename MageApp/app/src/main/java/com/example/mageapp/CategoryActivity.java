package com.example.mageapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mageapp.catalog.Category;
import com.example.mageapp.helper.DrawerItem;
import com.example.mageapp.xmlconnect.CategoryConntect;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends DefaultActivity {

    protected static final String TAG = "CategoryActivity";
    protected static final String ARG_CATEGORY_ID = "categoryId";
    protected static final String EXTRA_CATEGORY = "intent.EXTRA_CATEGORY";
    protected static final String SAVED_CATEGORY = "savedCategory";
    protected static final String SAVED_DRAWER_ITEMS = "savedDrawerItems";
    protected static final String SAVED_TITLE = "savedTitle";
    protected DrawerLayout mDrawerLayout;
    protected ListView mDrawerList;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected ListAdapter mDrawerListAdapter;
    protected String mCategoryId;
    protected Category mCategory;
    protected List<DrawerItem>  mDrawerItems = new ArrayList<>();
    protected CharSequence mTitle;
    protected CharSequence mDrawertitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        if (savedInstanceState != null) {
            mCategory = savedInstanceState.getParcelable(SAVED_CATEGORY);
            List<Parcelable> drawerItems =
                    savedInstanceState.getParcelableArrayList(SAVED_DRAWER_ITEMS);
            mDrawerItems = new ArrayList<>();
            for (Parcelable item : drawerItems) {
                mDrawerItems.add((DrawerItem) item);
            }
            mTitle = savedInstanceState.getCharSequence(SAVED_TITLE);
            this.setTitle(mTitle);
            this.setDrawer();
            this.updateDrawerAdapter();
        } else {
            mCategory = getIntent().getParcelableExtra(EXTRA_CATEGORY);
            new CategoryTask().execute(mCategory.getId());
            this.setTitle(mCategory.getLabel());
            this.setDrawer();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_CATEGORY, mCategory);
        ArrayList<Parcelable> drawerItems = new ArrayList<>();
        for (DrawerItem item : mDrawerItems) {
            drawerItems.add(item);
        }
        outState.putParcelableArrayList(SAVED_DRAWER_ITEMS, drawerItems);
        outState.putCharSequence(SAVED_TITLE, this.getSupportActionBar().getTitle());
    }

    protected void setDrawer() {
        mTitle = mDrawertitle = this.getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawertitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    public void updateUI() {
        updateDrawerAdapter();
        updateFragment();
    }

    protected void updateDrawerAdapter() {
        List<Category> items = mCategory.getChildren();
        this.addHomeMenuItem();
        if (!items.isEmpty()) {
            this.setDrawerItems(items);
        }
        mDrawerListAdapter = new DrawerListAdapter(this, R.layout.drawer_list_item, mDrawerItems);
        mDrawerList.setAdapter(mDrawerListAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrawerItem item = mDrawerItems.get(position);
                if (item.isHome()) {
                    NavUtils.navigateUpFromSameTask(CategoryActivity.this);
                    return;
                }
                new CategoryTask().execute(item.getId());
                mDrawerList.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(mDrawerList);
                setTitle(item.getLabel());
            }
        });
    }

    protected void updateFragment() {
        Fragment frgmnt = CategoryFragment.newInstance(mCategory);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, frgmnt)
                .commit();
    }

    protected void addHomeMenuItem() {
        if (mDrawerItems.isEmpty()) {
            DrawerItem item = new DrawerItem();
            String lblHome = getResources().getString(R.string.home);
            item.setLabel(lblHome);
            mDrawerItems.add(item);
        }
    }

    protected void setDrawerItems(List<Category> items) {
        for (Category cat : items) {
            DrawerItem item = new DrawerItem();
            item.setId(cat.getId());
            item.setLabel(cat.getLabel());
            mDrawerItems.add(item);
        }
    }

    protected void setTitle(String title) {
        mTitle = title;
        this.getSupportActionBar().setTitle(title);
    }

    private class DrawerListAdapter extends ArrayAdapter<DrawerItem> {
        protected int mResId;
        protected List<DrawerItem> mItems;

        public DrawerListAdapter(Context context, int resId, List items) {
            super(context, resId, items);
            mResId = resId;
            mItems = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(mResId, parent, false);
            }
            TextView label = convertView.findViewById(R.id.item_label);
            label.setText(mItems.get(position).getLabel());
            return convertView;
        }
    }

    private class CategoryTask extends AsyncTask<String, Void, Category> {
        @Override
        protected Category doInBackground(String... params) {
            String cid = params[0];
            Category c = new CategoryConntect(CategoryActivity.this)
                    .fetchCategoryInfoById(cid);
            return c;
        }

        @Override
        protected void onPostExecute(Category cat) {
            mCategory = cat;
            updateUI();
        }
    }
}
