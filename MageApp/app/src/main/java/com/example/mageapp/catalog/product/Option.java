package com.example.mageapp.catalog.product;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 9/2/17.
 */

public class Option implements Parcelable {
    protected String mCode;
    protected String mType;
    protected String mLabel;
    protected String mPrice;
    protected String mFormatedPrice;
    protected List<OptionValue> mValues = new ArrayList<>();
    protected String mIsRequred;
    protected boolean mRequired;
    protected boolean mDefaultValueSet = false;

    public static final Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel source) {
            return new Option(source);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[0];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mCode);
        out.writeString(mType);
        out.writeString(mLabel);
        out.writeString(mPrice);
        out.writeString(mFormatedPrice);
        out.writeTypedList(mValues);
        out.writeString(mIsRequred);
        out.writeByte((byte) (mRequired ? 1 : 0));
        out.writeByte((byte) (mDefaultValueSet ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Option(Parcel in) {
        mCode = in.readString();
        mType = in.readString();
        mLabel = in.readString();
        mPrice = in.readString();
        mFormatedPrice = in.readString();
        in.readTypedList(mValues, OptionValue.CREATOR);
        mIsRequred = in.readString();
        mRequired = (in.readByte() != 0);
        mDefaultValueSet = (in.readByte() != 0);
    }

    public Option() {}

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
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

    public String getFormatedPrice() {
        return mFormatedPrice;
    }

    public void setFormatedPrice(String formatedPrice) {
        mFormatedPrice = formatedPrice;
    }

    public String getIsRequred() {
        return mIsRequred;
    }

    public void setIsRequred(String isRequred) {
        mIsRequred = isRequred;
        mRequired = (isRequred != null) && isRequred.equals("1");
    }

    public boolean isRequired() {
        return mRequired;
    }

    public void setRequired(boolean required) {
        mRequired = required;
    }

    public List<OptionValue> getValues() {
        return mValues;
    }

    public void setValues(List<OptionValue> values) {
        mValues = values;
    }

    public List<OptionValue> getSpinnerValues() {
        if (!mDefaultValueSet) {
            OptionValue val = new OptionValue();
            val.setLabel("Please Select");
            mValues.add(0, val);
            mDefaultValueSet = true;
        }
        return mValues;
    }
}
