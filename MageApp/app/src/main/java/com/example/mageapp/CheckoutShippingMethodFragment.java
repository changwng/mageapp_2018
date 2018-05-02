package com.example.mageapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.example.mageapp.checkout.ShippingMethod;
import com.example.mageapp.checkout.ShippingMethodRate;
import com.example.mageapp.helper.Helper;
import com.example.mageapp.helper.RequestParam;
import com.example.mageapp.helper.RequestParamList;
import com.example.mageapp.xmlconnect.CheckoutShippingMethodConnect;
import com.example.mageapp.xmlconnect.ResponseMessage;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutShippingMethodFragment extends Fragment {

    protected static final String TAG = CheckoutShippingMethodFragment.class.getSimpleName();
    protected static final String STATE_SHIPPING_METHODS = "shippingMethods";
    protected ArrayList<ShippingMethod> mShippingMethods = new ArrayList<>();
    protected LinearLayout mRadioGroup;
    protected Context mContext;
    protected RequestParamList mPostData = new RequestParamList();
    protected ResponseMessage mRespMsg;
    protected Button mBtNext;

    public CheckoutShippingMethodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        mContext = getContext();
        new FetchMethodsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_checkout_shipping_method, container, false);
        mRadioGroup = v.findViewById(R.id.checkout_shipping_method_radio_group);
        mBtNext = v.findViewById(R.id.bt_checkout_shipping_method_continue);
        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveMethodTask().execute();
            }
        });
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STATE_SHIPPING_METHODS, mShippingMethods);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mShippingMethods = savedInstanceState.getParcelableArrayList(STATE_SHIPPING_METHODS);
            this.updateUI();
            this.updateNextButton();
        }
    }

    public static Fragment newInstance() {
        Fragment f = new CheckoutShippingMethodFragment();
        return f;
    }

    protected void updateUI() {
        if (!mShippingMethods.isEmpty()) {
            for (ShippingMethod method : mShippingMethods) {
                if (!method.getRates().isEmpty()) {
                    for (ShippingMethodRate rate : method.getRates()) {
                        RadioButton btn = new RadioButton(mContext);
                        btn.setTag(rate);
                        btn.setText(rate.getRadioButtonLabel());
                        int viewId = this.getViewIdByRate(rate);
                        btn.setId(viewId);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String rateCode = ((ShippingMethodRate) v.getTag()).getCode();
                                mPostData.get("shipping_method").add(rateCode);
                                updateNextButton();
                            }
                        });
                        mRadioGroup.addView(btn);
                    }
                }
            }
        }
    }

    protected void handleSaveMethodAfter() {
        String msg = mRespMsg.getText();
        Resources res = mContext.getResources();
        String title = res.getString(R.string.alert);
        new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mRespMsg.isSuccess()) {
                            Intent activity = CheckoutPaymentMethodActivity.newIntent(mContext);
                            startActivity(activity);
                        }
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    protected int getViewIdByRate(ShippingMethodRate rate) {
        switch (rate.getCode()) {
            case "flatrate_flatrate":
                return R.id.flatrate_flatrate;
            case "ups_GND":
                return R.id.ups_GND;
            case "ups_3DS":
                return R.id.ups_3DS;
            case "ups_1DA":
                return R.id.ups_1DA;
            default:
                return Helper.generateViewId();
        }
    }

    protected void updateNextButton() {
        if (mPostData.isEmpty()) {
            mBtNext.setEnabled(false);
        } else {
            mBtNext.setEnabled(true);
        }
    }

    private class FetchMethodsTask extends AsyncTask<Void, Void, ArrayList> {
        @Override
        protected ArrayList doInBackground(Void... params) {
            ArrayList<ShippingMethod> methods = new CheckoutShippingMethodConnect(mContext)
                    .fetchShippingMethods();
            return methods;
        }

        @Override
        protected void onPostExecute(ArrayList result) {
            mShippingMethods = result;
            updateUI();
        }
    }

    private class SaveMethodTask extends AsyncTask<Void, Void, ResponseMessage> {
        @Override
        protected ResponseMessage doInBackground(Void... params) {
            ResponseMessage msg = new CheckoutShippingMethodConnect(mContext)
                    .saveShippingMethod(mPostData);
            return msg;
        }

        @Override
        protected void onPostExecute(ResponseMessage result) {
            mRespMsg = result;
            handleSaveMethodAfter();
        }
    }
}