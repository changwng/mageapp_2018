package com.example.mageapp.xmlconnect;

import android.content.Context;
import android.util.Log;

import com.example.mageapp.catalog.Product;
import com.example.mageapp.catalog.product.Option;
import com.example.mageapp.catalog.product.OptionValue;
import com.example.mageapp.catalog.product.OptionValueRelation;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 9/2/17.
 */

public class ProductConnect extends DefaultConnect {

    protected static final String TAG = "ProductConnect";

    public ProductConnect(Context context) {
        super(context);
        mPath = "xmlconnect/catalog/product";
    }

    public Product fetchProductById(String id) {
        Log.d(TAG, "product id: " + id);
        setPostData("id", id);
        String url = getRequestUrl();
        String xml = getContentByUrl(url);
        Product p = getProductByXml(xml);
        return p;
    }

    protected Product getProductByXml(String xml) {
        Product p = new Product();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if ((eventType == XmlPullParser.START_TAG)) {
                    String name = parser.getName();
                    if (name.equals("entity_id")) {
                        p.setId(parser.nextText());
                    } else if (name.equals("name")) {
                        p.setName(parser.nextText());
                    } else if (name.equals("short_description")) {
                        p.setDescription(parser.nextText());
                    } else if (name.equals("link")) {
                        p.setLink(parser.nextText());
                    } else if (name.equals("icon")) {
                        p.setIcon(parser.nextText());
                    } else if (name.equals("in_stock")) {
                        p.setInSotck(parser.nextText());
                    } else if (name.equals("price")) {
                        String price = parser.getAttributeValue(null, "regular");
                        p.setPrice(price);
                    } else if (name.equals("options")) {
                        List<Option> options = getOptions(parser, eventType);
                        p.setOptions(options);
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, xppe.getMessage(), xppe);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage(), ioe);
        }
        return p;
    }

    protected List<Option> getOptions(XmlPullParser parser, int eventType) throws IOException,
            XmlPullParserException {
        List<Option> options = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("options")))) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("option")) {
                    Option option = new Option();
                    String code = parser.getAttributeValue(null, "code");
                    String type = parser.getAttributeValue(null, "type");
                    String label = parser.getAttributeValue(null, "label");
                    String price = parser.getAttributeValue(null, "price");
                    String formatedPrice = parser.getAttributeValue(null, "formated_price");
                    String isRequired = parser.getAttributeValue(null, "is_required");
                    option.setCode(code);
                    option.setType(type);
                    option.setLabel(label);
                    option.setPrice(price);
                    option.setFormatedPrice(formatedPrice);
                    option.setIsRequred(isRequired);
                    List<OptionValue> values = getOptionValues(parser, eventType);
                    option.setValues(values);
                    options.add(option);
                }
            }
            eventType = parser.next();
        }
        return options;
    }

    protected List<OptionValue> getOptionValues(XmlPullParser parser, int eventType) throws IOException,
            XmlPullParserException {
        List<OptionValue> values = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("option")))) {
            if ((eventType == XmlPullParser.START_TAG) && (parser.getName().equals("value"))) {
                OptionValue value = new OptionValue();
                String code = parser.getAttributeValue(null, "code");
                String label = parser.getAttributeValue(null, "label");
                String price = parser.getAttributeValue(null, "price");
                String formatedPrice = parser.getAttributeValue(null, "formatedPrice");
                value.setCode(code);
                value.setLabel(label);
                value.setPrice(price);
                value.setFormattedPrice(formatedPrice);
                OptionValueRelation relation = getRelation(parser, eventType);
                value.setRelation(relation);
                values.add(value);
            }
            eventType = parser.next();
        }
        return values;
    }

    protected OptionValueRelation getRelation(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        OptionValueRelation relation = new OptionValueRelation();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("value")))) {
            if ((eventType == XmlPullParser.START_TAG) && (parser.getName().equals("relation"))) {
                String to = parser.getAttributeValue(null, "to");
                relation.setTo(to);
                List<OptionValue> values = getRelationValues(parser, eventType);
                relation.setValues(values);
            }
            eventType = parser.next();
        }
        return relation;
    }

    protected List<OptionValue> getRelationValues(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        List<OptionValue> values = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("relation")))) {
            if ((eventType == XmlPullParser.START_TAG) && (parser.getName().equals("value"))) {
                String code = parser.getAttributeValue(null, "code");
                String label = parser.getAttributeValue(null, "label");
                String price = parser.getAttributeValue(null, "price");
                String formatedPrice = parser.getAttributeValue(null, "formated_price");
                OptionValue value = new OptionValue();
                value.setCode(code);
                value.setLabel(label);
                value.setPrice(price);
                value.setFormattedPrice(formatedPrice);
                values.add(value);
            }
            eventType = parser.next();
        }
        return values;
    }
}
