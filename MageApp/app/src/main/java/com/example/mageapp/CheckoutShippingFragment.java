package com.example.mageapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mageapp.form.Form;
import com.example.mageapp.helper.RequestParamList;
import com.example.mageapp.xmlconnect.CheckoutShippingConnect;
import com.example.mageapp.xmlconnect.ResponseMessage;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutShippingFragment extends CheckoutAddressFragment {

    public CheckoutShippingFragment() {
        // Required empty public constructor
        mPostData = new RequestParamList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        new ShippingTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_checkout_shipping, container, false);
        mAddressForm = v.findViewById(R.id.form_shipping);
        mBtSave = v.findViewById(R.id.bt_save_shipping);
        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsFormValid = true;
                prepareFormPostData();
                if (mIsFormValid) {
                    new ShippingSaveTask().execute();
                }
            }
        });
        return v;
    }

    public static Fragment newInstance() {
        return new CheckoutShippingFragment();
    }

    protected void onPostFormSave() {
        if (mFormSaveRespMsg.isSuccess()) {
            Intent activity = new Intent(getContext(), CheckoutShippingMethodActivity.class);
            startActivity(activity);
        } else {
            Toast.makeText(getContext(), mFormSaveRespMsg.getText(), Toast.LENGTH_LONG).show();
        }
    }

    private class ShippingTask extends AsyncTask<Void, Void, Form> {
        @Override
        protected Form doInBackground(Void... params) {
            Form form = new CheckoutShippingConnect(getContext()).fetchShippingForm();
            return form;
        }

        @Override
        protected void onPostExecute(Form result) {
            mForm = result;
            updateUI();
        }
    }

    private class ShippingSaveTask extends AsyncTask<Void, Void, ResponseMessage> {
        @Override
        protected ResponseMessage doInBackground(Void... var1) {
            ResponseMessage msg = new CheckoutShippingConnect(getContext()).saveShipping(mPostData);
            return msg;
        }

        @Override
        protected void onPostExecute(ResponseMessage result) {
            mFormSaveRespMsg = result;
            onPostFormSave();
        }
    }
}
