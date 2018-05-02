package com.example.mageapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mageapp.cart.CartInfo;
import com.example.mageapp.helper.SharedPref;
import com.example.mageapp.xmlconnect.CartConnect;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class InitService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this

    protected static final String TAG = InitService.class.getSimpleName();
    protected Context mContext;
    protected CartInfo mCartInfo;

    public InitService() {
        super(TAG);
        mContext = this;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // update cart qty

        new CartInfoTask().execute();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, InitService.class);
    }

    protected void updateCartInfo() {
        Log.d(TAG, "updateCartInfo() method stared..");
        String qty = mCartInfo.getSummaryQty();
        SharedPref.putCartitesmQty(this, qty);
        Log.d(TAG, "updateCartInfo() method ended..");
    }

    private class CartInfoTask extends AsyncTask<Void, Void, CartInfo> {
        @Override
        protected CartInfo doInBackground(Void... params) {
            CartInfo info = new CartConnect(mContext).fetchCartInfo();
            return info;
        }

        @Override
        protected void onPostExecute(CartInfo result) {
            mCartInfo = result;
            updateCartInfo();
        }
    }
}
