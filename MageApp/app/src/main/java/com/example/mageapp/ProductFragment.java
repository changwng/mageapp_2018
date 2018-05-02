package com.example.mageapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mageapp.cart.CartData;
import com.example.mageapp.catalog.Product;
import com.example.mageapp.catalog.product.Option;
import com.example.mageapp.catalog.product.OptionValue;
import com.example.mageapp.catalog.product.OptionValueRelation;
import com.example.mageapp.helper.Helper;
import com.example.mageapp.helper.ImgDownloader;
import com.example.mageapp.xmlconnect.CartConnect;
import com.example.mageapp.xmlconnect.ProductConnect;
import com.example.mageapp.xmlconnect.ResponseMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends DefaultFragment implements AdapterView.OnItemSelectedListener {

    protected static final String TAG = "ProductFragment";
    protected static final String ARG_PRODUCT_ID = "ARG_PRODUCT_ID";
    protected static final String STATE_PRODUCT = "product";

    protected TextView mTvProductName;
    protected ImageView mIvProductIcon;
    protected TextView mTvProductPrice;
    protected TextView mTvProductDescr;
    protected Button mBtBuyNow;
    protected LinearLayout mProductOptionsContainer;
    protected String mProductId;
    protected Product mProduct;
    protected ImgDownloader<ImageView> mImgDownloader;
    protected Map<String, String> mOptions = new HashMap<>();
    protected int mNumRequiredFields;
    protected CartData mCartData;
    protected ResponseMessage mRespMsg;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);

        Handler mainHandler = new Handler();
        mImgDownloader = new ImgDownloader<>(getContext(), mainHandler);
        mImgDownloader.setOnDownloadListener(new ImgDownloader.OnDownloadListener<ImageView>() {
            @Override
            public void onDownloaded(ImageView target, Bitmap bitmap) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                target.getLayoutParams().height = (drawable.getIntrinsicHeight() * Helper.IMAGE_SCALE_VALUE);
                target.setImageDrawable(drawable);
                Log.d(TAG, "drawable x: " + drawable.getIntrinsicWidth() + ", drawable y: " +
                        drawable.getIntrinsicHeight());
            }
        });
        mImgDownloader.start();
        mImgDownloader.getLooper();

        if (savedInstanceState == null) {
            Bundle args = getArguments();
            mProductId = args.getString(ARG_PRODUCT_ID);
            String[] params = {mProductId};
            new ProductTask().execute(params);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product, container, false);
        mTvProductName = v.findViewById(R.id.tv_product_name);
        mIvProductIcon = v.findViewById(R.id.iv_product_icon);
        mTvProductPrice = v.findViewById(R.id.tv_product_price);
        mTvProductDescr = v.findViewById(R.id.tv_product_descr);
        mProductOptionsContainer = v.findViewById(R.id.product_options_container);
        mBtBuyNow = v.findViewById(R.id.bt_buy_now);
        mBtBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedFields = getNumSelectedFields();
                if (selectedFields < mNumRequiredFields) {
                    Toast.makeText(getContext(), "Please select required field(s)",
                            Toast.LENGTH_LONG).show();
                } else {
                    mCartData = new CartData();
                    mCartData.setProductId(mProductId);
                    mCartData.setOptions(mOptions);
                    /*Intent activity = new Intent(getContext(), CartActivity.class);
                    activity.putExtra(CartActivity.INTENT_EXTRA_CART_DATA, cartData);
                    startActivity(activity);*/
                    new AddToCartTask().execute();
                }
            }
        });
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_PRODUCT, mProduct);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mProduct = savedInstanceState.getParcelable(STATE_PRODUCT);
            updateUI();
        }
    }

    protected int getNumSelectedFields() {
        int cnt = 0;
        for (String key : mOptions.keySet()) {
            if (mOptions.get(key) != null) {
                cnt++;
            }
        }
        return cnt;
    }

    public static ProductFragment newFragment(String id) {
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_ID, id);
        ProductFragment f = new ProductFragment();
        f.setArguments(args);
        return f;
    }

    public void updateUI() {
        if (mProduct == null) return;
        String productName = mProduct.getName();
        ((ProductActivity) this.getActivity()).getSupportActionBar().setTitle(productName);
        mTvProductName.setText(productName);
        mImgDownloader.queueImg(mIvProductIcon, mProduct.getIcon());
        mTvProductPrice.setText(mProduct.getPrice());
        mTvProductDescr.setText(mProduct.getDescription());

        // render options dynamically..
        renderOptions();
    }

    protected void renderOptions() {
        resetRequiredFields();
        List<Option> options = mProduct.getOptions();
        int numOptions = options.size();
        for (int i = 0; i < numOptions; i++) {
            LinearLayout optionLayout = new LinearLayout(getContext());
            /*optionLayout.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
            ));*/
            optionLayout.setOrientation(LinearLayout.VERTICAL);
            Option option = options.get(i);
            if (option.isRequired()) {
                // set label
                TextView tvOption = new TextView(getContext());
                tvOption.setText(option.getLabel());
                int viewId = Helper.generateViewId();
                tvOption.setId(viewId);
                optionLayout.addView(tvOption);
                // set value
                switch (option.getType()) {
                    case "select":
                        Spinner spValues = new Spinner(getContext());
                        spValues.setTag(option.getCode());
                        SpinnerAdapter adapter = new OptionValueAdapter(getContext(),
                                option.getSpinnerValues());
                        spValues.setAdapter(adapter);
                        spValues.setOnItemSelectedListener(this);
                        viewId = Helper.generateViewId();
                        spValues.setId(viewId);
                        optionLayout.addView(spValues);
                        break;
                }
                mNumRequiredFields++;
            }
            /*LayoutParams params = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    1.0f
            );*/
            int viewId = Helper.generateViewId();
            mProductOptionsContainer.addView(optionLayout); // , params);
        }
    }

    protected void resetRequiredFields() {
        mNumRequiredFields = 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        OptionValue val = (OptionValue) parent.getAdapter().getItem(position);
        mOptions.put((String) parent.getTag(), val.getCode());
        OptionValueRelation relation = val.getRelation();
        if (relation != null) {
            String to = relation.getTo();
            Spinner sp = getView().findViewWithTag(to);
            SpinnerAdapter adapter = new OptionValueAdapter(getContext(),
                    relation.getSpinnerValues());
            sp.setAdapter(adapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void updateCartResponse() {
        new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.alert)
                .setMessage(mRespMsg.getText())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mRespMsg.isSuccess()) {
                            Intent activity = CartActivity.newIntent(getContext());
                            startActivity(activity);
                        }
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private class OptionValueAdapter extends ArrayAdapter<OptionValue> {
        protected int mResource;
        protected List<OptionValue> mItems;
        protected TextView mTvLabel;

        public OptionValueAdapter(Context context, List<OptionValue> items) {
            super(context, android.R.layout.simple_spinner_item, items);
            mResource = android.R.layout.simple_spinner_item;
            mItems = items;
        }

        @Override
        public
        @NonNull
        View getView(int position, @Nullable View convertView,
                     @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
            }
            mTvLabel = convertView.findViewById(android.R.id.text1);
            OptionValue item = mItems.get(position);
            mTvLabel.setText(item.getLabel());
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = getView(position, convertView, parent);
            return view;
        }
    }

    private class ProductTask extends AsyncTask<String, Void, Product> {
        @Override
        protected Product doInBackground(String... params) {
            String id = params[0];
            if (id == null) return null;
            Product p = new ProductConnect(getContext()).fetchProductById(id);
            return p;
        }

        @Override
        protected void onPostExecute(Product result) {
            mProduct = result;
            updateUI();
        }
    }

    private class AddToCartTask extends AsyncTask<Void, Void, ResponseMessage> {
        @Override
        protected ResponseMessage doInBackground(Void... params) {
            ResponseMessage msg = new CartConnect(getContext()).addItemToCart(mCartData);
            return msg;
        }

        @Override
        protected void onPostExecute(ResponseMessage result) {
            mRespMsg = result;
            updateCartResponse();
        }
    }
}
