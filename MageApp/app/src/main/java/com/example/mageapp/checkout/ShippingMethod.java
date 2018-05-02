package com.example.mageapp.checkout;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 10/12/17.
 */

public class ShippingMethod implements Parcelable {
    protected String mLabel;
    protected List<ShippingMethodRate> mRates = new ArrayList<>();

    public static final Creator<ShippingMethod> CREATOR
            = new Creator<ShippingMethod>() {
        public ShippingMethod createFromParcel(Parcel in) {
            return new ShippingMethod(in);
        }

        public ShippingMethod[] newArray(int size) {
            return new ShippingMethod[size];
        }
    };

    private ShippingMethod(Parcel in) {
        mLabel = in.readString();
        in.readTypedList(mRates, ShippingMethodRate.CREATOR);
    }

    public ShippingMethod() {}

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public List<ShippingMethodRate> getRates() {
        return mRates;
    }

    public void setRates(List<ShippingMethodRate> rages) {
        mRates = rages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mLabel);
        out.writeTypedList(mRates);
    }
}
