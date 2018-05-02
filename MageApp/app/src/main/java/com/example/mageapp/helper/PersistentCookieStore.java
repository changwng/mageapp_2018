package com.example.mageapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by foo on 10/8/17.
 */

public class PersistentCookieStore implements CookieStore, Runnable {

    protected static final String TAG = PersistentCookieStore.class.getSimpleName();
    protected static final String PREF_COOKIE = "prefCookie";
    protected static final String KEY = "default";

    protected CookieStore mStore;
    protected Context mContext;
    protected SharedPreferences mPref;

    public PersistentCookieStore(Context context) {
        mContext = context;
        mPref = mContext.getSharedPreferences(PREF_COOKIE, Context.MODE_PRIVATE);

        // get the default in memory cookie store
        mStore = new CookieManager().getCookieStore();

        // todo: read in cookies from persistant storage and add them store
        this.readCookies();

        // add a shutdown hook to write out the in memory cookies
        Runtime.getRuntime().addShutdownHook(new Thread(this));
    }

    @Override
    public void run() {
        // todo: write cookies in store to persistent storage
        this.writeCookies();
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        mStore.add(uri, cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return mStore.get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return mStore.getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return mStore.getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return mStore.remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return mStore.removeAll();
    }

    protected void readCookies() {
        String val = mPref.getString(KEY, null);
        if (val != null) {
            Gson gson = new Gson();
            Type collectionType = new TypeToken<Set<HttpCookie>>() {
            }.getType();
            Set<HttpCookie> cookies = gson.fromJson(val, collectionType);
            for (HttpCookie cookie : cookies) {
                this.add(null, cookie);
            }
        }
    }

    protected void writeCookies() {
        List<HttpCookie> cookies = mStore.getCookies();
        Set<HttpCookie> vals = new HashSet<>();
        for (HttpCookie cookie : cookies) {
            vals.add(cookie);
        }
        Gson gson = new Gson();
        String json = gson.toJson(vals);
        mPref.edit().putString(KEY, json).apply();
    }
}