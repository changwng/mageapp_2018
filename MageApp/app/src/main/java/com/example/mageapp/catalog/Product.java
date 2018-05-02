package com.example.mageapp.catalog;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mageapp.catalog.product.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 5/29/17.
 */

public class Product implements Parcelable {

    protected String mId;
    protected String mName;
    protected String mLink;
    protected String mIcon;
    protected String mPrice;
    protected String mSpecialPrice;
    protected String mDescription;
    protected String mInSotck;
    protected List<Option> mOptions = new ArrayList<>();

    public static final Parcelable.Creator<Product> CREATOR
            = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    private Product(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mLink = in.readString();
        mIcon = in.readString();
        mPrice = in.readString();
        mSpecialPrice = in.readString();
        mDescription = in.readString();
        mInSotck = in.readString();
        in.readList(mOptions, Option.class.getClassLoader());
    }

    public Product() {
    }

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

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getSpecialPrice() {
        return mSpecialPrice;
    }

    public void setSpecialPrice(String specialPrice) {
        mSpecialPrice = specialPrice;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getInSotck() {
        return mInSotck;
    }

    public void setInSotck(String inSotck) {
        mInSotck = inSotck;
    }

    public List<Option> getOptions() {
        return mOptions;
    }

    public void setOptions(List<Option> options) {
        mOptions = options;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mLink);
        dest.writeString(mIcon);
        dest.writeString(mPrice);
        dest.writeString(mSpecialPrice);
        dest.writeString(mDescription);
        dest.writeString(mInSotck);
        dest.writeList(mOptions);
    }
}