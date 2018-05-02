package com.example.mageapp.checkout;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mageapp.form.Form;

/**
 * Created by foo on 10/14/17.
 */

public class PaymentMethod implements Parcelable {

    private static final PaymentMethod ourInstance = new PaymentMethod();

    protected String mId;
    protected String mCode;
    protected String mPostName;
    protected String mLabel;
    protected Form mForm;

    public static final Creator<PaymentMethod> CREATOR
            = new Creator<PaymentMethod>() {
        public PaymentMethod createFromParcel(Parcel in) {
            return new PaymentMethod(in);
        }

        public PaymentMethod[] newArray(int size) {
            return new PaymentMethod[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mId);
        out.writeString(mCode);
        out.writeString(mPostName);
        out.writeString(mLabel);
        out.writeParcelable(mForm, flags);
    }

    private PaymentMethod(Parcel in) {
        mId = in.readString();
        mCode = in.readString();
        mPostName = in.readString();
        mLabel = in.readString();
        in.readParcelable(mForm.getClass().getClassLoader());
    }

    public PaymentMethod() {}

    public static PaymentMethod getInstance() {
        return ourInstance;
    }

    /*private PaymentMethod() {
    }*/

    public static PaymentMethod getOurInstance() {
        return ourInstance;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getPostName() {
        return mPostName;
    }

    public void setPostName(String postName) {
        mPostName = postName;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public Form getForm() {
        return mForm;
    }

    public void setForm(Form form) {
        mForm = form;
    }
}
