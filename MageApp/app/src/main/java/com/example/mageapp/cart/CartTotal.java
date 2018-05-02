package com.example.mageapp.cart;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by foo on 9/26/17.
 */

public class CartTotal implements Parcelable {
    protected String mType;
    protected String mTitle;
    protected String mValue;
    protected String mFormatedValue;

    public static final Creator<CartTotal> CREATOR
            = new Creator<CartTotal>() {
        public CartTotal createFromParcel(Parcel in) {
            return new CartTotal(in);
        }

        public CartTotal[] newArray(int size) {
            return new CartTotal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mType);
        out.writeString(mTitle);
        out.writeString(mValue);
        out.writeString(mFormatedValue);
    }

    private CartTotal(Parcel in) {
        mType = in.readString();
        mTitle = in.readString();
        mValue = in.readString();
        mFormatedValue = in.readString();
    }

    public CartTotal() {

    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public String getFormatedValue() {
        return mFormatedValue;
    }

    public void setFormatedValue(String formatedValue) {
        mFormatedValue = formatedValue;
    }
}
