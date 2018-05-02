package com.example.mageapp.xmlconnect;

import android.content.Context;
import android.util.Log;

import com.example.mageapp.checkout.ShippingMethod;
import com.example.mageapp.checkout.ShippingMethodRate;
import com.example.mageapp.helper.RequestParam;
import com.example.mageapp.helper.RequestParamList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by foo on 10/12/17.
 */

public class CheckoutShippingMethodConnect extends DefaultConnect {

    protected static final String TAG = CheckoutShippingMethodConnect.class.getSimpleName();

    public CheckoutShippingMethodConnect(Context context) {
        super(context);
        mPath = "xmlconnect/checkout/shippingMethods";
    }

    public ArrayList<ShippingMethod> fetchShippingMethods() {
        String url = this.getRequestUrl();
        String resp = this.getContentByUrl(url);
//        String resp = this.getShippingMethodsResponse();
        ArrayList<ShippingMethod> methods = this.getShippingMethods(resp);
        return methods;
    }

    public String getShippingMethodsResponse() {
        return "<shipping_methods>\n" +
                "  <method label=\"Flat Rate\">\n" +
                "     <rates>\n" +
                "        <rate label=\"Fixed\" code=\"flatrate_flatrate\" price=\"5.00\" formated_price=\"$5.00\"/>\n" +
                "     </rates>\n" +
                "  </method>\n" +
                "  <method label=\"United Parcel Service\">\n" +
                "     <rates>\n" +
                "        <rate label=\"Ground\" code=\"ups_GND\" price=\"10.51\" formated_price=\"$10.51\"/>\n" +
                "        <rate label=\"3 Day Select\" code=\"ups_3DS\" price=\"28.32\" formated_price=\"$28.32\"/>\n" +
                "        <rate label=\"Next Day Air\" code=\"ups_1DA\" price=\"77.88\" formated_price=\"$77.88\"/>\n" +
                "     </rates>\n" +
                "  </method>\n" +
                "</shipping_methods>";
    }

    protected ArrayList<ShippingMethod> getShippingMethods(String xml) {
        ArrayList<ShippingMethod> methods = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tag = parser.getName();
                    if (tag.equals("method")) {
                        String label = parser.getAttributeValue(null, "label");
                        ShippingMethod method = new ShippingMethod();
                        method.setLabel(label);
                        List<ShippingMethodRate> rates = this.getShippingMethodRates(parser, eventType);
                        method.setRates(rates);
                        methods.add(method);
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, xppe.getMessage(), xppe);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage(), ioe);
        }
        return methods;
    }

    protected List<ShippingMethodRate> getShippingMethodRates(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        List<ShippingMethodRate> rates = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("rates")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("rate")) {
                    String label = parser.getAttributeValue(null, "label");
                    String code = parser.getAttributeValue(null, "code");
                    String price = parser.getAttributeValue(null, "price'");
                    String formatedPrice = parser.getAttributeValue(null, "formated_price");
                    ShippingMethodRate rate = new ShippingMethodRate();
                    rate.setLabel(label);
                    rate.setCode(code);
                    rate.setPrice(price);
                    rate.setFormatedPrice(formatedPrice);
                    rates.add(rate);
                }
            }
            eventType = parser.next();
        }
        return rates;
    }

    public ResponseMessage saveShippingMethod(RequestParamList data) {
        mPath = "xmlconnect/checkout/saveShippingMethod";
        mParams = data;
        String url = this.getRequestUrl();
        String resp = this.getContentByUrl(url);
//        String resp = this.getSaveMethodResponse();
        ResponseMessage msg = parseResponseMessage(resp);
        return msg;
    }

    protected String getSaveMethodResponse() {
        return "<message>\n" +
                "   <status>success</status>\n" +
                "   <text>Shipping method has been set.</text>\n" +
                "</message>";
    }
}