package com.example.mageapp.sales;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by foo on 10/23/17.
 */

public class QuoteItemOption implements Parcelable {
    protected String mLabel;
    protected String mText;

    public static final Creator<QuoteItemOption> CREATOR
            = new Creator<QuoteItemOption>() {
        public QuoteItemOption createFromParcel(Parcel in) {
            return new QuoteItemOption(in);
        }

        public QuoteItemOption[] newArray(int size) {
            return new QuoteItemOption[size];
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

    protected QuoteItemOption(Parcel in) {
        mLabel = in.readString();
        mText = in.readString();
    }

    public QuoteItemOption() {}

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
