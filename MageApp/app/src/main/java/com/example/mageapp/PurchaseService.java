package com.example.mageapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mageapp.helper.SharedPref;
import com.example.mageapp.rest.Soap;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class PurchaseService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this

    protected static final String TAG = PurchaseService.class.getName();

    public PurchaseService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, PurchaseService.class.getEnclosingMethod() + " called..");

        String lastOrderId = SharedPref.getLastOrderId(this);
        String orderId = new Soap().fetchLastOrderId();

    }

    protected static Intent newIntent(Context c) {
        return new Intent(c, PurchaseService.class);
    }
}
