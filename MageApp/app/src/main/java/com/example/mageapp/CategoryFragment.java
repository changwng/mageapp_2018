package com.example.mageapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mageapp.catalog.Category;
import com.example.mageapp.catalog.Product;
import com.example.mageapp.helper.Helper;
import com.example.mageapp.helper.ImgDownloader;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * Needs a "Navigation Drawer" feature to render hamburger menu..
 */
public class CategoryFragment extends DefaultFragment {

    protected static final String TAG = "CategoryFragment";
    protected static final int GRID_SPAN_CNT = 2;
    protected static final String ARG_CATEGORY = "ARG_CATEGORY";
    protected Category mCategory;
    protected RecyclerView mRecyclerView;
    protected RecyclerViewAdapter mRecyclerViewAdapter;
    protected ImgDownloader<ItemViewHolder> mImgDownloader;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        Bundle args = getArguments();
        mCategory = args.getParcelable(ARG_CATEGORY);
        mImgDownloader = new ImgDownloader<>(getContext(), new Handler());
        mImgDownloader.setOnDownloadListener(new ImgDownloader.OnDownloadListener<ItemViewHolder>() {
            @Override
            public void onDownloaded(ItemViewHolder holder, Bitmap bitmap) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                holder.bindDrawable(drawable);
                Log.d(TAG, "drawable w: " + drawable.getIntrinsicWidth() + ", drawable h: " +
                        drawable.getIntrinsicHeight());
            }
        });
        mImgDownloader.start();
        mImgDownloader.getLooper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), GRID_SPAN_CNT));
        mRecyclerViewAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
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

    public static Fragment newInstance(Category cat) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_CATEGORY, cat);
        Fragment frgmnt = new CategoryFragment();
        frgmnt.setArguments(args);
        return frgmnt;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected Product mItem;
        protected ImageView mIcon;
        protected TextView mLabel;
        protected TextView mPrice;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mIcon = itemView.findViewById(R.id.item_icon);
            mLabel = itemView.findViewById(R.id.item_label);
            mPrice = itemView.findViewById(R.id.item_price);
        }

        public void bindItem(Product item) {
            mItem = item;
            mLabel.setText(item.getName());
            mPrice.setText(item.getPrice());
        }

        public void bindDrawable(Drawable drawable) {
//            mIcon.requestLayout();
//            mIcon.getLayoutParams().width = (drawable.getIntrinsicWidth() * 3);
            mIcon.getLayoutParams().height = (drawable.getIntrinsicHeight() * Helper.IMAGE_SCALE_VALUE);
//            mIcon.setScaleType(ImageView.ScaleType.FIT_XY);
            mIcon.setImageDrawable(drawable);
        }

        public void onClick(View view) {
            Intent activity = ProductActivity.newIntent(getContext());
            activity.putExtra(ProductActivity.INTENT_EXTRA_PRODCT_ID, mItem.getId());
            startActivity(activity);
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        @Override
        public int getItemCount() {
            return mCategory.getProducts().size();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater infalater = LayoutInflater.from(getContext());
            View view = infalater.inflate(R.layout.category_grid_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            Product product = mCategory.getProducts().get(position);
            holder.bindItem(product);
            mImgDownloader.queueImg(holder, product.getIcon());
        }
    }
}
