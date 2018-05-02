package com.example.mageapp.helper;

/**
 * Created by foo on 11/25/17.
 */

public class RequestParam {
    protected String mKey;
    protected String mValue;

    public RequestParam(String key, String value) {
        mKey = key;
        mValue = value;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }
}
