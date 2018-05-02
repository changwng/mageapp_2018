package com.example.mageapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.net.HttpCookie;

/**
 * Created by foo on 6/11/17.
 */

public class SharedPref {

    protected static final String PREFS_LAST_ORDER_ID = "lastOrderId";
    protected static final String PREFS_HEADER_COOKIE = "headerCookie";
    protected static final String PREFS_CART_ITEMS_QTY = "cartItemsQty";

    protected static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void putLastOrderId(Context c, String id) {
        getSharedPreferences(c).edit()
                .putString(PREFS_LAST_ORDER_ID, id)
                .apply();
    }

    public static String getLastOrderId(Context c) {
        return getSharedPreferences(c).getString(PREFS_LAST_ORDER_ID, null);
    }

    public static void putHeaderCookie(Context c, HttpCookie cookie) {
        Gson gson = new Gson();
        String json = gson.toJson(cookie);
        getSharedPreferences(c).edit().putString(PREFS_HEADER_COOKIE, json).apply();
    }

    public static HttpCookie getHeaderCookie(Context c) {
        HttpCookie cookie = null;
        String json = getSharedPreferences(c).getString(PREFS_HEADER_COOKIE, null);
        if (json != null) {
            Gson gson = new Gson();
            cookie = gson.fromJson(json, HttpCookie.class);
        }
        return cookie;
    }

    public static void removeHeaderCookie(Context c) {
        getSharedPreferences(c).edit().remove(PREFS_HEADER_COOKIE).apply();
    }

    public static void putCartitesmQty(Context c, String qty) {
        getSharedPreferences(c).edit().putString(PREFS_CART_ITEMS_QTY, qty).apply();
    }

    public static String getCartItemsQty(Context c) {
        return getSharedPreferences(c).getString(PREFS_CART_ITEMS_QTY, null);
    }
}
