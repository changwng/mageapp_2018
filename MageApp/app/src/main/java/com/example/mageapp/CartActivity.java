package com.example.mageapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {

    protected static final String TAG = "CartActivity";
//    protected static final String INTENT_EXTRA_CART_DATA = "intent.extra.CART_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f == null) {
            f = CartFragment.getFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
        }
        this.getSupportActionBar().setTitle(R.string.shopping_cart);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, CartActivity.class);
    }
}