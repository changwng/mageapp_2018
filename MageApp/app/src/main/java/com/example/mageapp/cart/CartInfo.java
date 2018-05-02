package com.example.mageapp.cart;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 9/26/17.
 */

public class CartInfo implements Parcelable {
    protected String mIsVirtual;
    protected String mSummaryQty;
    protected String mVirtualQty;
    protected List<CartTotal> mTotals = new ArrayList<>();

    public static final Creator<CartInfo> CREATOR
            = new Creator<CartInfo>() {
        public CartInfo createFromParcel(Parcel in) {
            return new CartInfo(in);
        }

        public CartInfo[] newArray(int size) {
            return new CartInfo[size];
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
        out.writeString(mVirtualQty);
        out.writeTypedList(mTotals);
    }

    private CartInfo(Parcel in) {
        mIsVirtual = in.readString();
        mSummaryQty = in.readString();
        mVirtualQty = in.readString();
        in.readTypedList(mTotals, CartTotal.CREATOR);
    }

    public CartInfo() {}

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

    public String getVirtualQty() {
        return mVirtualQty;
    }

    public void setVirtualQty(String virtualQty) {
        mVirtualQty = virtualQty;
    }

    public List<CartTotal> getTotals() {
        return mTotals;
    }

    public void setTotals(List<CartTotal> totals) {
        mTotals = totals;
    }
}
