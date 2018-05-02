package com.example.mageapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mageapp.cart.CartTotal;
import com.example.mageapp.checkout.OrderReview;
import com.example.mageapp.helper.ImgDownloader;
import com.example.mageapp.helper.RequestParam;
import com.example.mageapp.helper.RequestParamList;
import com.example.mageapp.helper.SharedPref;
import com.example.mageapp.sales.QuoteItem;
import com.example.mageapp.sales.QuoteItemOption;
import com.example.mageapp.xmlconnect.CheckoutReviewConnect;
import com.example.mageapp.xmlconnect.ResponseMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutReviewFragment extends Fragment {

    protected static final String TAG = CheckoutReviewFragment.class.getSimpleName();
    protected static final String STATE_ORDER_REVIEW = "orderReview";
    protected Context mContext;
    protected OrderReview mOrderReview;
    protected RecyclerView mItemRecyclerView;
    protected ImgDownloader<ItemViewHolder> mImgDownloader;
    protected LinearLayout mllTotalTitle;
    protected LinearLayout mllTotalValue;
    protected Button mBtPlaceOrder;
    protected ResponseMessage mRespMsg;
    protected Bundle mPaymentData; // payment data from checkout payment
    protected RequestParamList mPostData = new RequestParamList();

    public CheckoutReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        mContext = this.getContext();
        new CheckoutReviewTask().execute();

        Handler responseHandler = new Handler();
        mImgDownloader = new ImgDownloader<>(mContext, responseHandler);
        mImgDownloader.setOnDownloadListener(new ImgDownloader.OnDownloadListener<ItemViewHolder>() {
            @Override
            public void onDownloaded(ItemViewHolder holder, Bitmap bitmap) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                holder.bindDrawable(drawable);
            }
        });
        mImgDownloader.start();
        mImgDownloader.getLooper();
        mPaymentData = this.getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_checkout_review, container, false);
        mItemRecyclerView = v.findViewById(R.id.checkout_review_list_item);
        LinearLayoutManager layoutMgr = new LinearLayoutManager(mContext);
        mItemRecyclerView.setLayoutManager(layoutMgr);
        mllTotalTitle = v.findViewById(R.id.ll_checkout_review_total_title);
        mllTotalValue = v.findViewById(R.id.ll_checkout_review_total_value);
        mBtPlaceOrder = v.findViewById(R.id.bt_place_order);
        mBtPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String key : mPaymentData.keySet()) {
                    String val = mPaymentData.getString(key);
                    mPostData.get(key).add(val);
                }
                new PlaceOrderTask().execute();
            }
        });
        return v;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_ORDER_REVIEW, mOrderReview);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mOrderReview = savedInstanceState.getParcelable(STATE_ORDER_REVIEW);
            this.updateUI();
        }
    }

    public static Fragment newInstance(Bundle paymentData) {
        Bundle args = new Bundle();
        args.putAll(paymentData);
        CheckoutReviewFragment f = new CheckoutReviewFragment();
        f.setArguments(args);
        return f;
    }

    protected void updateUI() {
        ItemViewAdapter adapter = new ItemViewAdapter(mOrderReview.getProducts());
        mItemRecyclerView.setAdapter(adapter);

        for (CartTotal total : mOrderReview.getTotals()) {
            TextView tvTitle = new TextView(mContext);
            String title = total.getTitle();
            if (total.getType().equals("shipping")) {
                title = "Shipping & Handling";
            }
            tvTitle.setText(title);
            mllTotalTitle.addView(tvTitle);

            TextView tvValue = new TextView(mContext);
            String val = total.getFormatedValue();
            tvValue.setText(val);
            mllTotalValue.addView(tvValue);
        }
    }

    protected void handlePlaceOrderAfter() {
        Resources res = this.getResources();
        String title = res.getString(R.string.alert);

        if (mRespMsg.isSuccess()) {
            SharedPref.putCartitesmQty(mContext, "0"); // update / clear saved cart items qty
            Intent activity = new CheckoutOrderSuccessActivity().newIntent(mContext);
            activity.putExtra(CheckoutOrderSuccessActivity.EXTRA_MESSAGE_TEXT,
                    mRespMsg.getText());
            startActivity(activity);
        } else {
            new AlertDialog.Builder(mContext)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(title)
                    .setMessage(mRespMsg.getText())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mIcon;
        protected TextView mName;
        protected TextView mQty;
        protected TextView mSubtotal;
        protected LinearLayout mOptionLabelContainer;
        protected LinearLayout mOptionTextContainer;
        public ItemViewHolder(View view) {
            super(view);
            mIcon = view.findViewById(R.id.iv_quote_item_icon);
            mQty = view.findViewById(R.id.tv_quote_item_qty);
            mSubtotal = view.findViewById(R.id.tv_quote_item_subtotal);
            mName = view.findViewById(R.id.tv_quote_item_name);
            mOptionLabelContainer = view.findViewById(R.id.ll_quote_item_option_label);
            mOptionTextContainer = view.findViewById(R.id.ll_quote_item_option_text);
        }
        public void bindItem(QuoteItem item) {
            mName.setText(item.getName());
            mQty.setText(item.getQty());
            mSubtotal.setText(item.getFormatedSubtotal());
            // set product option(s) here..
            for (QuoteItemOption option : item.getOptions()) {
                String label = option.getLabel();
                TextView tvLabel = new TextView(mContext);
                tvLabel.setText(label);
                mOptionLabelContainer.addView(tvLabel);
                String text = option.getText();
                TextView tvText = new TextView(mContext);
                tvText.setText(text);
                mOptionTextContainer.addView(tvText);
            }
        }
        public void bindDrawable(Drawable drawable) {
            mIcon.setImageDrawable(drawable);
        }
    }

    private class ItemViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        protected List<QuoteItem> mItems;
        public ItemViewAdapter(List<QuoteItem> items) {
            mItems = items;
        }
        @Override
        public int getItemCount() {
            return mItems.size();
        }
        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup root, int i) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.quote_list_item, root, false);
            return new ItemViewHolder(view);
        }
        @Override
        public void onBindViewHolder(ItemViewHolder holder, int pos) {
            QuoteItem item = mItems.get(pos);
            holder.bindItem(item);
            mImgDownloader.queueImg(holder, item.getIcon());
        }
    }

    private class CheckoutReviewTask extends AsyncTask<Void, Void, OrderReview> {
        @Override
        protected OrderReview doInBackground(Void... params) {
            OrderReview review = new CheckoutReviewConnect(mContext).fetccOrderReview();
            return review;
        }
        @Override
        protected void onPostExecute(OrderReview result) {
            mOrderReview = result;
            updateUI();
        }
    }

    private class PlaceOrderTask extends AsyncTask<Void, Void, ResponseMessage> {
        @Override
        protected ResponseMessage doInBackground(Void... params) {
            ResponseMessage respMsg = new CheckoutReviewConnect(mContext).placeOrder(mPostData);
            return respMsg;
        }
        @Override
        protected void onPostExecute(ResponseMessage result) {
            mRespMsg = result;
            handlePlaceOrderAfter();
        }
    }
}
