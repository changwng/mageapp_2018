package com.example.mageapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CheckoutShippingMethodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = this.getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f == null) {
            f = CheckoutShippingMethodFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
        }

        this.getSupportActionBar().setTitle(R.string.shipping_method);
    }
}