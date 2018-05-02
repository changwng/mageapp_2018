package com.example.mageapp.xmlconnect;

import android.content.Context;

import com.example.mageapp.form.Form;
import com.example.mageapp.helper.RequestParam;
import com.example.mageapp.helper.RequestParamList;

import java.util.List;

/**
 * Created by foo on 10/7/17.
 */

public class CheckoutBillingConnect extends CheckoutAddressConnect {

    public CheckoutBillingConnect(Context c) {
        super(c);
    }

    public Form fetchBillingForm() {
        mPath = "xmlconnect/checkout/newBillingAddressForm";
        String url = this.getRequestUrl();
        String resp = getContentByUrl(url);
        return this.parseFormByXml(resp);
    }

    public ResponseMessage saveBilling(RequestParamList postData) {
        mPath = "xmlconnect/checkout/saveBillingAddress";
        mParams = postData;
        String url = this.getRequestUrl();
        String resp = getContentByUrl(url);
        return this.parseResponseMessage(resp);
    }
}