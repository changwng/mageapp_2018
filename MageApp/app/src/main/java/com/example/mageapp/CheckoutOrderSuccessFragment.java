package com.example.mageapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutOrderSuccessFragment extends Fragment {

    protected static final String ARG_MESSAGE_TEXT = "ARG_MESSAGE_TEXT";
    protected static final String STATE_MESSAGE_TEXT = "messageText";
    protected String mMsgTxt;
    protected TextView mTvMessage;
    protected Button mBtContShop;

    public CheckoutOrderSuccessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        mMsgTxt = this.getArguments().getString(ARG_MESSAGE_TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chckout_order_success, container, false);
        mTvMessage = v.findViewById(R.id.tv_checkout_order_success_message);
        mBtContShop = v.findViewById(R.id.bt_continue_shopping);
        mBtContShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = MainActivity.newIntent(getContext());
                startActivity(activity);
            }
        });
        this.updateHeaderText();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_MESSAGE_TEXT, mMsgTxt);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mMsgTxt = savedInstanceState.getString(STATE_MESSAGE_TEXT);
            this.updateHeaderText();
        }
    }

    public static Fragment newInstance(String msgTxt) {
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE_TEXT, msgTxt);
        Fragment f = new CheckoutOrderSuccessFragment();
        f.setArguments(args);
        return f;
    }

    protected void updateHeaderText() {
        mTvMessage.setText(mMsgTxt);
    }
}