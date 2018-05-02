package com.example.mageapp.form;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by foo on 9/30/17.
 */

public class FormFieldValidator implements Parcelable {
    protected String mId;
    protected String mType;
    protected String mMessage;
    protected String mRelation;

    public static final Creator<FormFieldValidator> CREATOR
            = new Creator<FormFieldValidator>() {
        public FormFieldValidator createFromParcel(Parcel in) {
            return new FormFieldValidator(in);
        }

        public FormFieldValidator[] newArray(int size) {
            return new FormFieldValidator[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mId);
        out.writeString(mType);
        out.writeString(mMessage);
        out.writeString(mRelation);
    }

    private FormFieldValidator(Parcel in) {
        mId = in.readString();
        mType = in.readString();
        mMessage = in.readString();
        mRelation = in.readString();
    }

    public FormFieldValidator() {}

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getRelation() {
        return mRelation;
    }

    public void setRelation(String relation) {
        mRelation = relation;
    }
}
