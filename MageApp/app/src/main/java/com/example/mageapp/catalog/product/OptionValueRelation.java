package com.example.mageapp.catalog.product;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by foo on 9/2/17.
 */

public class OptionValueRelation implements Parcelable {

    protected String mTo;
    protected List<OptionValue> mValues;
    protected boolean mDefaultValueSet = false;

    public static final Creator<OptionValueRelation> CREATOR
            = new Creator<OptionValueRelation>() {
        public OptionValueRelation createFromParcel(Parcel in) {
            return new OptionValueRelation(in);
        }

        public OptionValueRelation[] newArray(int size) {
            return new OptionValueRelation[size];
        }
    };

    private OptionValueRelation(Parcel in) {
        mTo = in.readString();
        in.readTypedList(mValues, OptionValue.CREATOR);
        mDefaultValueSet = (in.readByte() != 0);
    }

    public OptionValueRelation() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTo);
        out.writeTypedList(mValues);
        out.writeByte((byte) (mDefaultValueSet ? 1 : 0));
    }

    public String getTo() {
        return mTo;
    }

    public void setTo(String to) {
        mTo = to;
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
