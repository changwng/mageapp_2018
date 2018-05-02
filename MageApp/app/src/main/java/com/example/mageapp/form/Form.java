package com.example.mageapp.form;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 9/30/17.
 */

public class Form implements Parcelable {
    protected String mId;
    protected String mName;
    protected String mAction;
    protected String mMethod;
    protected List<FormField> mFields = new ArrayList<>();

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

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        mAction = action;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
        mMethod = method;
    }

    public List<FormField> getFields() {
        return mFields;
    }

    public void setFields(List<FormField> fields) {
        mFields = fields;
    }

    public void addField(FormField field) {
        mFields.add(field);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.mAction);
        dest.writeString(this.mMethod);
        dest.writeList(this.mFields);
    }

    public Form() {
    }

    protected Form(Parcel in) {
        this.mId = in.readString();
        this.mName = in.readString();
        this.mAction = in.readString();
        this.mMethod = in.readString();
        this.mFields = new ArrayList<FormField>();
        in.readList(this.mFields, FormField.class.getClassLoader());
    }

    public static final Creator<Form> CREATOR = new Creator<Form>() {
        @Override
        public Form createFromParcel(Parcel source) {
            return new Form(source);
        }

        @Override
        public Form[] newArray(int size) {
            return new Form[size];
        }
    };
}
