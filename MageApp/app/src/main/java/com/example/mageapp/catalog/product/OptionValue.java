package com.example.mageapp.catalog.product;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by foo on 9/2/17.
 */

public class OptionValue implements Parcelable {
    protected String mCode;
    protected String mLabel;
    protected String mPrice;
    protected String mFormatedPrice;
    protected OptionValueRelation mRelation;

    public static final Creator<OptionValue> CREATOR
            = new Creator<OptionValue>() {
        public OptionValue createFromParcel(Parcel in) {
            return new OptionValue(in);
        }

        public OptionValue[] newArray(int size) {
            return new OptionValue[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeString(mCode);
        out.writeString(mLabel);
        out.writeString(mPrice);
        out.writeString(mFormatedPrice);
        out.writeParcelable(mRelation, flags);
    }

    private OptionValue(Parcel in) {
        mCode = in.readString();
        mLabel = in.readString();
        mPrice = in.readString();
        mFormatedPrice = in.readString();
        in.readParcelable(OptionValueRelation.class.getClassLoader());
    }

    public OptionValue() {}

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getFormattedPrice() {
        return mFormatedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        mFormatedPrice = formattedPrice;
    }

    public OptionValueRelation getRelation() {
        return mRelation;
    }

    public void setRelation(OptionValueRelation relation) {
        mRelation = relation;
    }
}
