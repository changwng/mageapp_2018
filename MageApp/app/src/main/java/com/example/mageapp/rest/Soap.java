package com.example.mageapp.rest;

import android.net.Uri;
import android.util.Log;

import com.example.mageapp.helper.Helper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by foo on 8/26/17.
 */

public class Soap {
    protected static final String TAG = "Soap";
    protected static final String ENDPOINT = "http://mage.testing.acacloud.com";
    protected static final String REQUEST_PATH = "index.php/api/v2_soap";
    protected static final String REQUEST_PARAM_KEY = "wsdl";
    protected static final String REQUEST_PARAM_VALUE = "1";
    protected static final String API_USER = "apiUser";
    protected static final String API_KEY = "apiKey";
    protected static final String NAME_SPACE = "urn:Magento";
    protected static final String SOAP_ACTION = "urn:Action";
    protected Uri mUri;
    protected String mUrl; // http://mage.testing.acacloud.com/api/soap/?wsdl
    protected String mSessId;

    public Soap() {
        init();
    }

    protected void init() {
        mUri = Uri.parse(ENDPOINT).buildUpon()
                .appendEncodedPath(REQUEST_PATH)
//                .appendQueryParameter(REQUEST_PARAM_KEY, REQUEST_PARAM_VALUE)
                .build();
        mUrl = mUri.toString();
    }

    public String fetchLastOrderId() {
        String id = null;
        try {
            id = getLastOrderId();
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage(), ioe);
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, xppe.getMessage(), xppe);
        }
        return id;
    }

    protected String getLastOrderId() throws IOException, XmlPullParserException {
        String id = null;

        SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        env.dotNet = false;
        env.xsd = SoapSerializationEnvelope.XSD;
        env.enc = SoapSerializationEnvelope.ENC;

        SoapObject req = new SoapObject(NAME_SPACE, "login");
        req.addProperty("username", API_USER);
        req.addProperty("apiKey", API_KEY);

        env.setOutputSoapObject(req);

        HttpTransportSE trans = new HttpTransportSE(mUrl);

        trans.call(SOAP_ACTION, env);
        Object resp = env.getResponse();

        mSessId = resp.toString();

        String dateOffset = getOrderDateOffset();

        SoapObject cFilter = new SoapObject();
        cFilter.addProperty("key", "gt");
        cFilter.addProperty("value", dateOffset);

        SoapObject cFilterArr = new SoapObject();
        cFilterArr.addProperty("key", "created_at");
        cFilterArr.addProperty("value", cFilter);

        cFilter = new SoapObject();
        cFilter.addProperty("complexFilterArray", cFilterArr);

        SoapObject filters = new SoapObject();
        filters.addProperty("complex_filter", cFilter);

        req = new SoapObject(NAME_SPACE, "salesOrderList");
        req.addProperty("sessionId", mSessId);
        req.addProperty("filters", filters);

        env.setOutputSoapObject(req);
        trans.call(SOAP_ACTION, env);
        resp = env.getResponse();


        return id;
    }

    protected String getOrderDateOffset() {
        // subtract 10 minutes from now
        Calendar cal = Calendar.getInstance();
        int min = (cal.get(Calendar.MINUTE) - 10);
        cal.set(Calendar.MINUTE, min);
        Date date = cal.getTime();
        return Helper.getGMT(date);
    }
}
