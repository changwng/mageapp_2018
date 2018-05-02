package com.example.mageapp.xmlconnect;

import android.content.Context;

import com.example.mageapp.form.Form;
import com.example.mageapp.helper.RequestParamList;

/**
 * Created by foo on 10/7/17.
 */

public class CheckoutShippingConnect extends CheckoutAddressConnect {

    public CheckoutShippingConnect(Context c) {
        super(c);
    }

    public Form fetchShippingForm() {
        mPath = "xmlconnect/checkout/newShippingAddressForm";
        String url = this.getRequestUrl();
        String resp = getContentByUrl(url);
        return this.parseFormByXml(resp);
    }

    public ResponseMessage saveShipping(RequestParamList postData) {
        mPath = "xmlconnect/checkout/saveShippingAddress";
        mParams = postData;
        String url = this.getRequestUrl();
        String resp = getContentByUrl(url);
        return this.parseResponseMessage(resp);
    }
}
