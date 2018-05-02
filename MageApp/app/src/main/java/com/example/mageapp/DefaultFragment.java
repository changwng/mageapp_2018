package com.example.mageapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mageapp.cart.CartInfo;
import com.example.mageapp.helper.SharedPref;

/**
 * A simple {@link Fragment} subclass.
 */
public class DefaultFragment extends Fragment {

    protected static final String TAG = DefaultFragment.class.getSimpleName();
    protected CartInfo mCartInfo;
    protected TextView mTvCartBadge;

    public DefaultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        new CartInfoTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_default, container, false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        this.updateMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_option, menu);

        final MenuItem item = menu.findItem(R.id.shopping_cart);
        View actionView = item.getActionView();
        mTvCartBadge = actionView.findViewById(R.id.tv_cart_badge);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item);
            }
        });
        this.updateMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shopping_cart:
                Intent activity = new Intent(getContext(), CartActivity.class);
                startActivity(activity);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.invalidateOptionsMenu();
    }

    protected void invalidateOptionsMenu() {
        this.getActivity().invalidateOptionsMenu();
    }

    protected void updateMenu() {
//        mTvCartBadge.setText(mCartInfo.getSummaryQty());
        String qty = SharedPref.getCartItemsQty(getContext());
        if (qty == null) return;
        if (qty.equals("0")) {
            mTvCartBadge.setVisibility(View.GONE);
        } else {
            mTvCartBadge.setText(qty);
            this.invalidateOptionsMenu();
        }
    }
}
