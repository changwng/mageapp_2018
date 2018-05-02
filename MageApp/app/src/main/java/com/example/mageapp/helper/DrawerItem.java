package com.example.mageapp.helper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by foo on 9/23/17.
 */

public class DrawerItem implements Parcelable {
    protected static final String HOME_ID = "home";
    protected String mId;
    protected String mLabel;
    public static Parcelable.Creator<DrawerItem> CREATOR = new Parcelable.Creator<DrawerItem>() {
        @Override
        public DrawerItem[] newArray(int size) {
            return new DrawerItem[size];
        }

        @Override
        public DrawerItem createFromParcel(Parcel source) {
            return new DrawerItem(source);
        }
    };

    public DrawerItem() {
    }

    public DrawerItem(Parcel source) {
        mId = source.readString();
        mLabel = source.readString();
    }

    public String getId() {
        return (mId == null) ? HOME_ID : mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public boolean isHome() {
        return this.getId().equals(HOME_ID);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mLabel);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
