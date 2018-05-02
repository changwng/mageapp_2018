package com.example.mageapp.checkout;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by foo on 10/13/17.
 */

public class ShippingMethodRate implements Parcelable {
    protected String mLabel;
    protected String mCode;
    protected String mPrice;
    protected String mFormatedPrice;

    public static final Creator<ShippingMethodRate> CREATOR
            = new Creator<ShippingMethodRate>() {
        public ShippingMethodRate createFromParcel(Parcel in) {
            return new ShippingMethodRate(in);
        }

        public ShippingMethodRate[] newArray(int size) {
            return new ShippingMethodRate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mLabel);
        out.writeString(mCode);
        out.writeString(mPrice);
        out.writeString(mFormatedPrice);
    }

    private ShippingMethodRate(Parcel in) {
        mLabel = in.readString();
        mCode = in.readString();
        mPrice = in.readString();
        mFormatedPrice = in.readString();
    }

    public ShippingMethodRate() {}

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getFormatedPrice() {
        return mFormatedPrice;
    }

    public void setFormatedPrice(String formatedPrice) {
        mFormatedPrice = formatedPrice;
    }

    public String getRadioButtonLabel() {
        return String.format("%s: %s", mLabel, mFormatedPrice);
    }
}
