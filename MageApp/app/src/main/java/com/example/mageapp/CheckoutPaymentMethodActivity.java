package com.example.mageapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CheckoutPaymentMethodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = this.getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f == null) {
            f = CheckoutPaymentMethodFragment.newFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
        }

        this.getSupportActionBar().setTitle(R.string.payment_info);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, CheckoutPaymentMethodActivity.class);
    }
}
