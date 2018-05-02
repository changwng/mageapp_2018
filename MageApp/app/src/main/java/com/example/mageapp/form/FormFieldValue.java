package com.example.mageapp.form;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 9/30/17.
 */

public class FormFieldValue implements DropDownItem, Parcelable {
    protected String mRelation;
    protected String mLabel;
    protected String mValue;
    protected List<FormFieldValue> mItems = new ArrayList<>();

    public static final Creator<FormFieldValue> CREATOR
            = new Creator<FormFieldValue>() {
        public FormFieldValue createFromParcel(Parcel in) {
            return new FormFieldValue(in);
        }

        public FormFieldValue[] newArray(int size) {
            return new FormFieldValue[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mRelation);
        out.writeString(mLabel);
        out.writeString(mValue);
        out.writeList(mItems);
    }

    private FormFieldValue(Parcel in) {
        mRelation = in.readString();
        mLabel = in.readString();
        mValue = in.readString();
        in.readList(mItems, FormFieldValue.class.getClassLoader());
    }

    public FormFieldValue() {}

    public String getRelation() {
        return mRelation;
    }

    public void setRelation(String relation) {
        mRelation = relation;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public List<FormFieldValue> getItems() {
        return mItems;
    }

    public void setItems(List<FormFieldValue> items) {
        mItems = items;
    }

    public String getItemLabel() {
        return mLabel;
    }
}
