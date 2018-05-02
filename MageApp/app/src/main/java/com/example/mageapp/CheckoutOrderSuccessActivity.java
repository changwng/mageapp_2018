package com.example.mageapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class CheckoutOrderSuccessActivity extends AppCompatActivity {

    protected static final String EXTRA_MESSAGE_TEXT =
            "com.example.mageapp.intent.extra.MESSAGE_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        FragmentManager fm = this.getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f == null) {
            String msgTxt = getIntent().getStringExtra(EXTRA_MESSAGE_TEXT);
            f = CheckoutOrderSuccessFragment.newInstance(msgTxt);
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
        }
    }

    protected static Intent newIntent(Context context) {
        return new Intent(context, CheckoutOrderSuccessActivity.class);
    }
}
