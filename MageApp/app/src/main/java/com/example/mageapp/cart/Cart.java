package com.example.mageapp.cart;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 9/6/17.
 */

public class Cart implements Parcelable {
    protected String mIsVirtual;
    protected String mSummaryQty;
    protected List<CartItem> mItems = new ArrayList<>();

    public static final Creator<Cart> CREATOR
            = new Creator<Cart>() {
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mIsVirtual);
        out.writeString(mSummaryQty);
        out.writeTypedList(mItems);
    }

    private Cart(Parcel in) {
        mIsVirtual = in.readString();
        mSummaryQty = in.readString();
        in.readTypedList(mItems, CartItem.CREATOR);
    }

    public Cart() {}

    public String getIsVirtual() {
        return mIsVirtual;
    }

    public void setIsVirtual(String isVirtual) {
        mIsVirtual = isVirtual;
    }

    public String getSummaryQty() {
        return mSummaryQty;
    }

    public void setSummaryQty(String summaryQty) {
        mSummaryQty = summaryQty;
    }

    public List<CartItem> getItems() {
        return mItems;
    }

    public void setItems(List<CartItem> items) {
        mItems = items;
    }

    public boolean hasItem() {
        return (!mItems.isEmpty());
    }
}
