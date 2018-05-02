package com.example.mageapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mageapp.cart.Cart;
import com.example.mageapp.cart.CartInfo;
import com.example.mageapp.cart.CartItem;
import com.example.mageapp.cart.CartItemOption;
import com.example.mageapp.cart.CartTotal;
import com.example.mageapp.helper.ImgDownloader;
import com.example.mageapp.helper.SharedPref;
import com.example.mageapp.xmlconnect.CartConnect;
import com.example.mageapp.xmlconnect.ResponseMessage;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    protected static final String TAG = "CartFragment";
    protected static final String STATE_CART = "cart";
    protected static final String STATE_CART_INFO = "cartInfo";

    //    protected CartData mCartData;
    protected RecyclerView mCartRecyclerView;
    protected Button mBtnCheckOut;
    protected Button mBtnStartShopping;
    protected ImageView mIvCartEmpty;
    protected ImgDownloader<CartItemHolder> mImgDoownloader;
    protected Cart mCart;
    protected CartInfo mCartInfo;
    protected LinearLayout mCartTotalContainer;
    protected LinearLayout mCartTotalLabel;
    protected LinearLayout mCartTotalValue;
    protected ResponseMessage mResMsg;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        mImgDoownloader = new ImgDownloader<>(getContext(), new Handler());
        mImgDoownloader.setOnDownloadListener(new ImgDownloader.OnDownloadListener<CartItemHolder>() {
            @Override
            public void onDownloaded(CartItemHolder target, Bitmap bitmap) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                target.bindDrawable(drawable);
            }
        });
        mImgDoownloader.start();
        mImgDoownloader.getLooper();
        renderUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mImgDoownloader != null) {
            mImgDoownloader.clearQueue();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImgDoownloader != null) {
            mImgDoownloader.quit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        mCartRecyclerView = v.findViewById(R.id.cart_recycler_view);
        LayoutManager layoutMgr = new LinearLayoutManager(container.getContext());
        mCartRecyclerView.setLayoutManager(layoutMgr);
        mBtnCheckOut = v.findViewById(R.id.btn_cart_checkout);
        mBtnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(getContext(), CheckoutBillingActivity.class);
                startActivity(activity);
            }
        });
        mBtnStartShopping = v.findViewById(R.id.btn_cart_start_shopping);
        mBtnStartShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = MainActivity.newIntent(getContext());
                startActivity(activity);
            }
        });
        mIvCartEmpty = v.findViewById(R.id.iv_cart_empty);
        mCartTotalContainer = v.findViewById(R.id.cart_total_container);
        mCartTotalLabel = v.findViewById(R.id.cart_total_label);
        mCartTotalValue = v.findViewById(R.id.cart_total_value);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_CART, mCart);
        outState.putParcelable(STATE_CART_INFO, mCartInfo);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mCart = savedInstanceState.getParcelable(STATE_CART);
            mCartInfo = savedInstanceState.getParcelable(STATE_CART_INFO);
            updateUI();
            updateCartInfoUI();
        }
    }

    public static Fragment getFragment() {
        return new CartFragment();
    }

    protected void renderUI() {
        new CartTask().execute();
        new CartInfoTask().execute();
    }

    protected void updateUI() {
        CartItemAdapter adapter = new CartItemAdapter(mCart.getItems());
        mCartRecyclerView.setAdapter(adapter);
        if (mCart.getItems().isEmpty()) {
            mBtnCheckOut.setVisibility(View.GONE);
            mBtnStartShopping.setVisibility(View.VISIBLE);
            mIvCartEmpty.setVisibility(View.VISIBLE);
//            mCartTotalContainer.setVisibility(View.GONE);
        } else {
            mBtnCheckOut.setVisibility(View.VISIBLE);
            mBtnStartShopping.setVisibility(View.GONE);
            mIvCartEmpty.setVisibility(View.GONE);
        }
    }

    protected void updateCartInfoUI() {
        // show cart total information
        SharedPref.putCartitesmQty(getContext(), mCartInfo.getSummaryQty());
        this.updateCartTotal();
//        updateMenu();
    }

    protected void updateCartTotal() {
        mCartTotalLabel.removeAllViews();
        mCartTotalValue.removeAllViews();
        Context context = getContext();
        List<CartTotal> totals = mCartInfo.getTotals();
        int numTotals = totals.size();
        for (int i = 0; i < numTotals; i++) {
            LinearLayout line = new LinearLayout(context);
            CartTotal total = totals.get(i);
            TextView label = new TextView(context);
            TextView val = new TextView(context);
            label.setText(total.getTitle());
//            label.setGravity(Gravity.RIGHT);
            val.setText(total.getFormatedValue());
            mCartTotalLabel.addView(label);
            mCartTotalValue.addView(val);
        }
    }

    protected void removeItemAfter() {
        new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.alert)
                .setMessage(mResMsg.getText())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mResMsg.isSuccess()) {
//                            new CartInfoTask().execute();
                            renderUI();
                            if (mCartInfo != null) {
                                String qty = mCartInfo.getSummaryQty();
                                SharedPref.putCartitesmQty(getContext(), qty);
//                                updateMenu();
                            }
//                            Intent activty = MainActivity.newIntent(getContext());
//                            startActivity(activty);
                        }
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private class CartItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView mIvIcon;
        protected TextView mTvName;
        protected TextView mTvQty;
        protected TextView mTvPrice;
        protected LinearLayout mOptionLabel;
        protected LinearLayout mOptionText;
        protected ImageButton mBtRemoveItem;
        protected CartItem mItem;

        public CartItemHolder(View view) {
            super(view);
            mIvIcon = view.findViewById(R.id.iv_cart_item_icon);
            mTvName = view.findViewById(R.id.tv_cart_item_name);
            mTvQty = view.findViewById(R.id.tv_cart_item_qty);
            mTvPrice = view.findViewById(R.id.tv_cart_item_price);
            mOptionLabel = view.findViewById(R.id.cart_item_option_label);
            mOptionText = view.findViewById(R.id.cart_item_option_text);
            mBtRemoveItem = view.findViewById(R.id.bt_cart_item_remove);

            mIvIcon.setOnClickListener(this);
            mBtRemoveItem.setOnClickListener(this);
        }

        public void bindItem(CartItem item) {
            mItem = item;
            mTvName.setText(item.getName());
            mTvQty.setText(item.getQty());
            String price = ((item.getFormatedPrice() != null) ? item.getFormatedPrice() : item.getPrice());
            mTvPrice.setText(price);
            renderOptions(item);
        }

        public void bindDrawable(Drawable drawable) {
            mIvIcon.setImageDrawable(drawable);
        }

        protected void renderOptions(CartItem item) {
            List<CartItemOption> options = item.getOptions();
            for (CartItemOption option : options) {
                String label = option.getLabel();
                String text = option.getText();

                TextView tvOptionLabel = new TextView(getContext());
                tvOptionLabel.setText(label);
                mOptionLabel.addView(tvOptionLabel);

                TextView tvOptionText = new TextView(getContext());
                tvOptionText.setText(text);
                mOptionText.addView(tvOptionText);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_cart_item_icon:
                    Intent activity = ProductActivity.newIntent(getContext());
                    activity.putExtra(ProductActivity.INTENT_EXTRA_PRODCT_ID, mItem.getEntityId());
                    startActivity(activity);
                    break;
                case R.id.bt_cart_item_remove:
                    String[] params = {mItem.getItemId()};
                    new RemoveItemTask().execute(params);
                    break;
            }
        }
    }

    private class CartItemAdapter extends RecyclerView.Adapter<CartItemHolder> {
        protected List<CartItem> mItems;

        public CartItemAdapter(List<CartItem> items) {
            mItems = items;
        }

        @Override
        public CartItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.cart_list_item, parent, false);
            return new CartItemHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CartItemHolder holder, int position) {
            CartItem item = mItems.get(position);
            holder.bindItem(item);
            mImgDoownloader.queueImg(holder, item.getIcon());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    private class CartTask extends AsyncTask<Void, Void, Cart> {
        @Override
        protected Cart doInBackground(Void... params) {
            Cart cart = new CartConnect(getContext()).fetchCart();
            return cart;
        }

        @Override
        protected void onPostExecute(Cart result) {
            mCart = result;
            updateUI();
        }
    }

    private class CartInfoTask extends AsyncTask<Void, Void, CartInfo> {
        @Override
        protected CartInfo doInBackground(Void... var1) {
            CartInfo info = new CartConnect(getContext()).fetchCartInfo();
            return info;
        }

        @Override
        protected void onPostExecute(CartInfo result) {
            mCartInfo = result;
            updateCartInfoUI();
        }
    }

    private class RemoveItemTask extends AsyncTask<String, Void, ResponseMessage> {
        @Override
        protected ResponseMessage doInBackground(String... params) {
            String itemId = params[0];
            return new CartConnect(getContext()).removeItem(itemId);
        }

        @Override
        protected void onPostExecute(ResponseMessage result) {
            mResMsg = result;
            removeItemAfter();
        }
    }
}