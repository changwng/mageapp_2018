package com.example.mageapp.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mageapp.R;

import java.util.List;

/**
 * Created by foo on 10/18/17.
 */

public class DropDownAdapter extends ArrayAdapter {
    protected Context mContext;
    protected int mResource;
    protected List<DropDownItem> mItems;
    protected TextView mLabel;

    public DropDownAdapter(Context context, int resource, List items) {
        super(context, resource, items);
        mContext = context;
        mResource = resource;
        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }
        mLabel = convertView.findViewById(R.id.spinner_label);
        DropDownItem item = this.getItem(position);
        mLabel.setText(item.getItemLabel());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return this.getView(position, convertView, parent);
    }

    @Override
    public DropDownItem getItem(int position) {
        return mItems.get(position);
    }
}