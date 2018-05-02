package com.example.mageapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mageapp.catalog.Category;
import com.example.mageapp.helper.ImgDownloader;
import com.example.mageapp.xmlconnect.DefaultConnect;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends DefaultFragment {

    protected static final String TAG = "MainFragment";
    protected RecyclerView mRecyclerView;
    protected List<Category> mItems = new ArrayList<>();
    protected ImgDownloader<ViewHolder> mImgDownloader;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new ConnectTask().execute();

        Handler responseHandler = new Handler();
        mImgDownloader = new ImgDownloader<>(getContext(), responseHandler);
        mImgDownloader.setOnDownloadListener(new ImgDownloader.OnDownloadListener<ViewHolder>() {
            @Override
            public void onDownloaded(ViewHolder holder, Bitmap bitmap) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                holder.bindDrawable(drawable);
            }
        });
        mImgDownloader.start();
        mImgDownloader.getLooper();

        Intent service = PurchaseService.newIntent(getContext());
        getContext().startService(service);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = view.findViewById(R.id.category_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateAdapter();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mImgDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImgDownloader.quit();
    }

    public void updateAdapter() {
        if (mItems == null) return;
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext());
        mRecyclerView.setAdapter(adapter);
    }

    public static Fragment getFragment() {
        return new MainFragment();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView mIcon;
        protected TextView mLabel;
        protected Category mItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.category_icon);
            mLabel = itemView.findViewById(R.id.category_label);
            itemView.setOnClickListener(this);
        }

        public void bindItem(Category item) {
            mItem = item;
            mLabel.setText(item.getLabel());
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getContext(), CategoryActivity.class);
            i.putExtra(CategoryActivity.EXTRA_CATEGORY, mItem);
            getContext().startActivity(i);
        }

        public void bindDrawable(Drawable drawable) {
            mIcon.setImageDrawable(drawable);
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
        protected Context mContext;

        public RecyclerViewAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View itemView = inflater.inflate(R.layout.category_list_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Category item = mItems.get(position);
            holder.bindItem(item);
            mImgDownloader.queueImg(holder, item.getIcon());
        }
    }

    private class ConnectTask extends AsyncTask<Void, Void, List<Category>> {
        @Override
        protected List<Category> doInBackground(Void... params) {
            List<Category> items = new DefaultConnect(getContext()).fetchCategoryItems();
            return items;
        }

        @Override
        protected void onPostExecute(List<Category> items) {
            mItems = items;
            updateAdapter();
        }
    }
}