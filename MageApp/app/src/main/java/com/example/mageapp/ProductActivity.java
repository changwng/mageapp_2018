package com.example.mageapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class ProductActivity extends DefaultActivity {

    protected static final String TAG = "ProductActivity";
    protected static final String INTENT_EXTRA_PRODCT_ID = "intent.extra.PRODUCT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f == null) {
            String productId = getIntent().getStringExtra(INTENT_EXTRA_PRODCT_ID);
            f = ProductFragment.newFragment(productId);
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ProductActivity.class);
    }
}