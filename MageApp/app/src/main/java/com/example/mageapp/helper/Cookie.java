package com.example.mageapp.helper;

import android.content.Context;
import android.util.Log;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by foo on 3/11/17.
 */

public class Cookie {

    protected static final String TAG = Cookie.class.getSimpleName();
    public static final String REQUEST_HEADER_KEY = "Cookie";
    public static final String RESPONSE_HEADER_KEY = "Set-Cookie";
    public static final String DELIMITER = ";";

    protected static Cookie sCookie;
    protected Context mContext;
    protected String mDomain;
    protected String mPath;
    protected int mVersion;
    protected long mMaxAge;
    protected boolean mSecure;
    protected URI mUri;

    private Cookie(Context context) {
        mContext = context;
        mDomain = Helper.getDomain();
        mPath = "/";
        mVersion = 0;
        mMaxAge = (60 * 60); // 1 hour
        mSecure = true;
//        CookieStore store = new PersistentCookieStore(mContext);
        CookieHandler.setDefault(new CookieManager());
        setUri();
    }

    public CookieStore getStore() {
        CookieManager mgr = (CookieManager) CookieHandler.getDefault();
        return mgr.getCookieStore();
    }

    public static Cookie getInstance(Context context) {
        if (sCookie == null) {
            sCookie = new Cookie(context);
        }
        return sCookie;
    }

    public void add(String name, String value) {
        HttpCookie cookie = new HttpCookie(name, value);
        cookie.setDomain(mDomain);
        cookie.setPath(mPath);
        cookie.setVersion(mVersion);
        if (mMaxAge > 0) {
            cookie.setMaxAge(mMaxAge);
        }
        cookie.setSecure(mSecure);
        getStore().add(null, cookie);
    }

    public void add(HttpCookie cookie) {
        mDomain = cookie.getDomain();
        mPath = cookie.getPath();
        mVersion = cookie.getVersion();
        mMaxAge = cookie.getMaxAge();
        mSecure = cookie.getSecure();
        getStore().add(null, cookie);
    }

    public void addHeaderCookie(String headerCookie) {
        List<HttpCookie> cookies = HttpCookie.parse(headerCookie);
        int numCookies = cookies.size();
        for (int i = 0; i < numCookies; i++) {
            HttpCookie cookie = cookies.get(i);
            mDomain = cookie.getDomain();
            mPath = cookie.getPath();
            mVersion = cookie.getVersion();
            mMaxAge = cookie.getMaxAge();
            mSecure = cookie.getSecure();
            getStore().add(null, cookie);
            SharedPref.putHeaderCookie(mContext, cookie);
        }
    }

    public List<HttpCookie> getCookies() {
        return getStore().getCookies();
    }

    protected void setUri() {
        String s = String.format("http://%s/", mDomain);
        try {
            mUri = new URI(s);
        } catch (URISyntaxException use) {
            Log.e(TAG, use.getMessage(), use);
        }
    }

    public void removeHeaderCookie() {
        SharedPref.removeHeaderCookie(mContext);
    }
}