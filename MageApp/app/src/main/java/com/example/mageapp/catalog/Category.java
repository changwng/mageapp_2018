package com.example.mageapp.catalog;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 3/11/17.
 */

public class Category implements Parcelable {
    protected String mId;
    protected String mLabel;
    protected String mIcon;
    protected List<Category> mChildren = new ArrayList<>();
    protected List<Product> mProducts = new ArrayList<>();

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public Category() {
    }

    private Category(Parcel in) {
        mId = in.readString();
        mLabel = in.readString();
        mIcon = in.readString();
        in.readTypedList(mChildren, Category.CREATOR);
        in.readTypedList(mProducts, Product.CREATOR);
    }

    public Category(String id, String label, String icon) {
        mId = id;
        mLabel = label;
        mIcon = icon;
    }

    public String getId() {
        return mId;
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

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public List<Category> getChildren() {
        return mChildren;
    }

    public void setChildren(List<Category> children) {
        mChildren = children;
    }

    public List<Product> getProducts() {
        return mProducts;
    }

    public void setProducts(List<Product> products) {
        mProducts = products;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mLabel);
        dest.writeString(mIcon);
        dest.writeTypedList(mChildren);
        dest.writeTypedList(mProducts);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Category fromJson(String json) {
        return new Gson().fromJson(json, Category.class);
    }
}