package com.example.mageapp.cart;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by foo on 9/6/17.
 */

public class CartItemOption implements Parcelable {
    protected String mLabel;
    protected String mText;

    public static final Creator<CartItemOption> CREATOR
            = new Creator<CartItemOption>() {
        public CartItemOption createFromParcel(Parcel in) {
            return new CartItemOption(in);
        }

        public CartItemOption[] newArray(int size) {
            return new CartItemOption[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mLabel);
        out.writeString(mText);
    }

    private CartItemOption(Parcel in) {
        mLabel = in.readString();
        mText = in.readString();
    }

    public CartItemOption() {}

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
