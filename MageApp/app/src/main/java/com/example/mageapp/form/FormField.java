package com.example.mageapp.form;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 9/30/17.
 */

public class FormField implements Parcelable {

    public static final String TYPE_CHECKBOX = "checkbox";
    public static final String TYPE_SELECT = "select";
    public static final String TYPE_TEXT = "text";

    protected String mId;
    protected String mName;
    protected String mLabel;
    protected String mType;
    protected String mRequired;
    protected List<FormFieldValidator> mValidators = new ArrayList<>();
    protected List<FormFieldValue> mValues = new ArrayList<>();

    public static final Creator<FormField> CREATOR
            = new Creator<FormField>() {
        public FormField createFromParcel(Parcel in) {
            return new FormField(in);
        }

        public FormField[] newArray(int size) {
            return new FormField[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mId);
        out.writeString(mName);
        out.writeString(mLabel);
        out.writeString(mType);
        out.writeString(mRequired);
        out.writeList(mValidators);
        out.writeList(mValues);
    }

    private FormField(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mLabel = in.readString();
        mType = in.readString();
        mRequired = in.readString();
        in.readList(mValidators, FormFieldValidator.class.getClassLoader());
        in.readList(mValues, FormFieldValidator.class.getClassLoader());
    }

    public FormField() {}

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getRequired() {
        return mRequired;
    }

    public void setRequired(String required) {
        mRequired = required;
    }

    public boolean isRequred() {
        return ((mRequired != null) && mRequired.equals("true"));
    }

    public List<FormFieldValidator> getValidators() {
        return mValidators;
    }

    public void setValidators(List<FormFieldValidator> validators) {
        mValidators = validators;
    }

    public List<FormFieldValue> getValues() {
        return mValues;
    }

    public void setValues(List<FormFieldValue> values) {
        mValues = values;
    }
}
