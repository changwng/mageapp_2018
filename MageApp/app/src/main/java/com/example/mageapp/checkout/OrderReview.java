package com.example.mageapp.checkout;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mageapp.cart.CartTotal;
import com.example.mageapp.sales.QuoteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 10/19/17.
 */

public class OrderReview implements Parcelable {
    protected List<QuoteItem> mItems = new ArrayList<>();
    protected List<CartTotal> mTotals = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(mItems);
        out.writeTypedList(mTotals);
    }

    public List<QuoteItem> getProducts() {
        return mItems;
    }

    public void setProducts(List<QuoteItem> items) {
        mItems = items;
    }

    public List<CartTotal> getTotals() {
        return mTotals;
    }

    public void setTotals(List<CartTotal> totals) {
        mTotals = totals;
    }
}