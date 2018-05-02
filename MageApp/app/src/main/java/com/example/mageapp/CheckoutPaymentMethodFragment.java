package com.example.mageapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mageapp.checkout.PaymentMethod;
import com.example.mageapp.form.DropDownAdapter;
import com.example.mageapp.form.FormField;
import com.example.mageapp.form.FormFieldValue;
import com.example.mageapp.helper.RequestParamList;
import com.example.mageapp.xmlconnect.CheckoutPaymentMethodConnect;
import com.example.mageapp.xmlconnect.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutPaymentMethodFragment extends Fragment {

    protected static final String TAG = CheckoutPaymentMethodFragment.class.getSimpleName();
    protected static final String STATE_METHODS = "paymentMethods";
    protected Context mContext;
    protected List<PaymentMethod> mMethods;
    protected RequestParamList mPostData = new RequestParamList();
//    protected LinearLayout mFormContainer;
//    protected TextView mTvPaymentMethodTitle;
    protected TextView mTvCardTypeTitle;
    protected Spinner mSpCardType;
    protected EditText mEtCardNumber;
    protected TextView mTvCardExpMonthTitle;
    protected Spinner mSpCardExpMonth;
    protected TextView mTvCardExpYearTitle;
    protected Spinner mSpCardExpYear;
//    protected TextView mTvCardVerifValTitle;
    protected EditText mEtCardVerifVal;
    protected Button mBtContinue;
    protected ResponseMessage mRespMsg;

    public CheckoutPaymentMethodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        mContext = this.getContext();
        new FetchPaymentMethodsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_checkout_payment_method, container, false);
//        mFormContainer = v.findViewById(R.id.checkout_payment_method_form_container);
//        mTvPaymentMethodTitle = v.findViewById(R.id.tv_payment_method_title);
        mTvCardTypeTitle = v.findViewById(R.id.card_type_title);
        mSpCardType = v.findViewById(R.id.card_type);
        mEtCardNumber = v.findViewById(R.id.card_number);
        mTvCardExpMonthTitle = v.findViewById(R.id.tv_card_exp_month_title);
        mSpCardExpMonth = v.findViewById(R.id.card_exp_month);
        mTvCardExpYearTitle = v.findViewById(R.id.tv_card_exp_year_title);
        mSpCardExpYear = v.findViewById(R.id.card_exp_year);
//        mTvCardVerifValTitle = v.findViewById(R.id.tv_card_verif_val_title);
        mEtCardVerifVal = v.findViewById(R.id.et_card_verif_val);
        mBtContinue = v.findViewById(R.id.bt_payment_method_continue);
        mBtContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    new SavePaymentTask().execute();
                }
            }
        });
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STATE_METHODS, ((ArrayList) mMethods));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mMethods = savedInstanceState.getParcelableArrayList(STATE_METHODS);
            updateUI();
        }
    }

    public static Fragment newFragment() {
        return new CheckoutPaymentMethodFragment();
    }

    protected void updateUI() {
        for (PaymentMethod method : mMethods) {
            String postName = method.getPostName();
            String code = method.getCode();
            mPostData.get(postName).add(code);
//            mTvPaymentMethodTitle.setText(method.getLabel());
//            mFormContainer.addView(tv);
            for (FormField field : method.getForm().getFields()) {
                switch (field.getName()) {
                    case "payment[cc_type]":
                        this.renderSpinner(field, mSpCardType, mTvCardTypeTitle, R.id.cc_type);
                        break;
                    case "payment[cc_number]":
                        this.renderEditText(field, mEtCardNumber, R.id.cc_number);
                        break;
                    case "payment[cc_exp_month]":
                        this.renderSpinner(field, mSpCardExpMonth, mTvCardExpMonthTitle, R.id.cc_exp_month);
                        break;
                    case "payment[cc_exp_year]":
                        this.renderSpinner(field, mSpCardExpYear, mTvCardExpYearTitle, R.id.cc_exp_year);
                        break;
                    case "payment[cc_cid]":
                        this.renderEditText(field, mEtCardVerifVal, R.id.cc_cid);
                        break;
                }
            }
        }
    }

    public void renderSpinner(FormField field, Spinner spinner, TextView txtView, int viewId) {
        txtView.setText(field.getLabel());
        DropDownAdapter adapter = new DropDownAdapter(mContext,
                R.layout.spinner_item, field.getValues());
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setTag(field);
        spinner.setId(viewId);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FormField parentField = (FormField) parent.getTag();
                FormFieldValue val = parentField.getValues().get(position);
                String name = parentField.getName();
                String value = val.getValue();
                mPostData.get(name).add(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void renderEditText(FormField field, EditText editTxt, int viewId) {
        editTxt.setHint(field.getLabel());
        editTxt.setTag(field);
        editTxt.setId(viewId);
        final String name = field.getName();
        editTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPostData.get(name).add(s.toString());
            }
        });
    }

    protected boolean validateForm() {
        for (PaymentMethod method : mMethods) {
            for (FormField field : method.getForm().getFields()) {
                String name = field.getName();
                if (!mPostData.containsKey(name) || mPostData.get(name).isEmpty()) {
                    this.openAlertDialog(String.format("%s cannot be empty", field.getLabel()));
                    return false;
                }
            }
        }
        return true;
    }

    protected void openAlertDialog(String msg) {
        new AlertDialog.Builder(mContext)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.alert)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    protected void onPostPaymentSave() {
        this.openAlertDialog(mRespMsg.getText());
        if (mRespMsg.isSuccess()) {
            Intent activity = CheckoutReviewActivity.newIntent(mContext);
            Bundle paymentData = new Bundle();
            for (String key : mPostData.keySet()) {
                List<String> items = mPostData.get(key);
                for (String val : items) {
                    paymentData.putString(key, val);
                }
            }
            activity.putExtra(CheckoutReviewActivity.EXTRA_PAYMENT_DATA, paymentData);
            this.startActivity(activity);
        }
    }

    private class FetchPaymentMethodsTask extends AsyncTask<Void, Void, ArrayList> {
        @Override
        protected ArrayList<PaymentMethod> doInBackground(Void... params) {
            ArrayList<PaymentMethod> methods = new CheckoutPaymentMethodConnect(mContext)
                    .fetchPaymentMethods();
            return methods;
        }
        @Override
        protected void onPostExecute(ArrayList result) {
            mMethods = result;
            updateUI();
        }
    }

    private class SavePaymentTask extends AsyncTask<Void, Void, ResponseMessage> {
        @Override
        protected ResponseMessage doInBackground(Void... params) {
            ResponseMessage msg = new CheckoutPaymentMethodConnect(mContext).savePayment(mPostData);
            return msg;
        }
        @Override
        protected void onPostExecute(ResponseMessage result) {
            mRespMsg = result;
            onPostPaymentSave();
        }
    }
}
