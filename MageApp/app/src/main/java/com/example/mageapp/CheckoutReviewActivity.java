package com.example.mageapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CheckoutReviewActivity extends AppCompatActivity {

    public static final String EXTRA_PAYMENT_DATA =
            "com.example.mageapp.intent.extra.PAYMENT_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        FragmentManager fm = this.getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f == null) {
            Bundle paymentData = this.getIntent().getBundleExtra(EXTRA_PAYMENT_DATA);
            if (paymentData == null) {
                paymentData = new Bundle();
            }
            f = CheckoutReviewFragment.newInstance(paymentData);
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
        }
        this.getSupportActionBar().setTitle(R.string.order_review);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, CheckoutReviewActivity.class);
    }
}
