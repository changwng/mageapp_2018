package com.example.mageapp.xmlconnect;

import android.content.Context;
import android.util.Log;

import com.example.mageapp.form.Form;
import com.example.mageapp.form.FormField;
import com.example.mageapp.form.FormFieldValidator;
import com.example.mageapp.form.FormFieldValue;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 9/30/17.
 */

public class CheckoutAddressConnect extends DefaultConnect {

    protected static final String TAG = "CheckoutAddressConnect";

    public CheckoutAddressConnect(Context c) {
        super(c);
    }

    protected Form parseFormByXml(String xml) {
        Form form = new Form();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xml));
            int et = xpp.getEventType();
            while (et != XmlPullParser.END_DOCUMENT) {
                if (et == XmlPullParser.START_TAG) {
                    String tag = xpp.getName();
                    if (tag.equals("form")) {
                        String id = xpp.getAttributeValue(null, "id");
                        String name = xpp.getAttributeValue(null, "name");
                        String action = xpp.getAttributeValue(null, "action");
                        String method = xpp.getAttributeValue(null, "method");
                        form.setId(id);
                        form.setName(name);
                        form.setAction(action);
                        form.setMethod(method);
                    } else if (tag.equals("field")) {
                        FormField field = this.parseFormField(xpp, et);
                        form.addField(field);
                    }
                }
                et = xpp.next();
            }
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, xppe.getMessage(), xppe);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage(), ioe);
        }
        return form;
    }

    protected FormField parseFormField(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        FormField field = new FormField();
        while (eventType != XmlPullParser.END_TAG) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("field")) {
                    String id = parser.getAttributeValue(null, "id");
                    String name = parser.getAttributeValue(null, "name");
                    String label = parser.getAttributeValue(null, "label");
                    String type = parser.getAttributeValue(null, "type");
                    String required = parser.getAttributeValue(null, "required");
                    field.setId(id);
                    field.setName(name);
                    field.setLabel(label);
                    field.setType(type);
                    field.setRequired(required);
                } else if (tag.equals("validators")) {
                    List<FormFieldValidator> validators =
                            this.parseFormFieldValidators(parser, eventType);
                    field.setValidators(validators);
                } else if (tag.equals("values")) {
                    List<FormFieldValue> values = this.parseFormFieldValues(parser, eventType);
                    field.setValues(values);
                }
            }
            eventType = parser.next();
        }
        return field;
    }

    protected List<FormFieldValidator> parseFormFieldValidators(XmlPullParser parser,
                                                                int eventType)
            throws IOException, XmlPullParserException {
        List<FormFieldValidator> validators = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("validators")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("validator")) {
                    String id = parser.getAttributeValue(null, "id");
                    String type = parser.getAttributeValue(null, "type");
                    String message = parser.getAttributeValue(null, "message");
                    FormFieldValidator validator = new FormFieldValidator();
                    validator.setId(id);
                    validator.setType(type);
                    validator.setMessage(message);
                    validators.add(validator);
                }
            }
            eventType = parser.next();
        }
        return validators;
    }

    protected List<FormFieldValue> parseFormFieldValues(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        List<FormFieldValue> values = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("values")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("item")) {
                    String relation = parser.getAttributeValue(null, "relation");
                    FormFieldValue val = new FormFieldValue();
                    val.setRelation(relation);
                    this.parseCheckoutFormFieldValue(parser, eventType, val);
                    values.add(val);
                }
            }
            eventType = parser.next();
        }
        return values;
    }

    protected FormFieldValue parseCheckoutFormFieldValue(XmlPullParser parser, int eventType,
                                                         FormFieldValue val)
            throws IOException, XmlPullParserException {
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("item")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("label")) {
                    val.setLabel(parser.nextText());
                } else if (tag.equals("value")) {
                    val.setValue(parser.nextText());
                } else if (tag.equals("regions")) {
                    List<FormFieldValue> regions = parseFormFieldRegions(parser, eventType);
                    val.setItems(regions);
                }
            }
            eventType = parser.next();
        }
        return val;
    }

    protected List<FormFieldValue> parseFormFieldRegions(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        List<FormFieldValue> regions = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("regions")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("region_item")) {
                    FormFieldValue region = parseFormFieldRegion(parser, eventType);
                    regions.add(region);
                }
            }
            eventType = parser.next();
        }
        return regions;
    }

    protected FormFieldValue parseFormFieldRegion(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        FormFieldValue region = new FormFieldValue();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("region_item")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("label")) {
                    region.setLabel(parser.nextText());
                } else if (tag.equals("value")) {
                    region.setValue(parser.nextText());
                }
            }
            eventType = parser.next();
        }
        return region;
    }
}