package com.example.mageapp.cart;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by foo on 9/3/17.
 */

public class CartData implements Parcelable {

    protected String mProductId;
    protected Map<String, String> mOptions = new HashMap<>();

    public static final Parcelable.Creator<CartData> CREATOR
            = new Parcelable.Creator<CartData>() {
        public CartData createFromParcel(Parcel in) {
            return new CartData(in);
        }

        public CartData[] newArray(int size) {
            return new CartData[size];
        }
    };

    public CartData(Parcel in) {
        mProductId = in.readString();
        in.readMap(mOptions, String.class.getClassLoader());
    }

    public CartData() {

    }

    public String getProductId() {
        return mProductId;
    }

    public void setProductId(String productId) {
        mProductId = productId;
    }

    public Map<String, String> getOptions() {
        return mOptions;
    }

    public void setOptions(Map<String, String> options) {
        mOptions = options;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mProductId);
        out.writeMap(mOptions);
    }
}