package com.example.mageapp.xmlconnect;

import android.content.Context;
import android.util.Log;

import com.example.mageapp.checkout.PaymentMethod;
import com.example.mageapp.form.Form;
import com.example.mageapp.form.FormField;
import com.example.mageapp.form.FormFieldValidator;
import com.example.mageapp.form.FormFieldValue;
import com.example.mageapp.helper.RequestParamList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 10/14/17.
 */

public class CheckoutPaymentMethodConnect extends DefaultConnect {

    public CheckoutPaymentMethodConnect(Context context) {
        super(context);
    }

    public ArrayList<PaymentMethod> fetchPaymentMethods() {
        mPath = "xmlconnect/checkout/paymentMethods";
        String url = this.getRequestUrl();
        String resp = this.getContentByUrl(url);
//        String resp = this.getPaymentMethodsResponse();
//        String resp = this.getPaymentMethodsResponse2();
        return this.getPaymentMethods(resp);
    }

    protected String getPaymentMethodsResponse() {
        return "<payment_methods>\n" +
                "   <method id=\"checkmo\" code=\"checkmo\" post_name=\"payment[method]\" label=\"Check / Money order\"/>\n" +
                "   <method id=\"purchaseorder\" code=\"purchaseorder\" post_name=\"payment[method]\" label=\"Purchase Order\">\n" +
                "      <form name=\"payment_form_purchaseorder\" method=\"post\">\n" +
                "         <fieldset>\n" +
                "            <field name=\"payment[po_number]\" type=\"text\" label=\"Purchase Order Number\" value=\"\" required=\"true\"/>\n" +
                "         </fieldset>\n" +
                "      </form>\n" +
                "   </method>\n" +
                "   <method id=\"authorizenet\" code=\"authorizenet\" post_name=\"payment[method]\" label=\"Credit Card (Authorize.net)\">\n" +
                "      <form name=\"payment_form_authorizenet\" method=\"post\">\n" +
                "         <fieldset>\n" +
                "            <field name=\"payment[cc_type]\" type=\"select\" label=\"Credit Card Type\" required=\"true\">\n" +
                "               <values>\n" +
                "                  <item>\n" +
                "                     <label>American Express</label>\n" +
                "                     <value>AE</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>Visa</label>\n" +
                "                     <value>VI</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>MasterCard</label>\n" +
                "                     <value>MC</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>Discover</label>\n" +
                "                     <value>DI</value>\n" +
                "                  </item>\n" +
                "               </values>\n" +
                "            </field>\n" +
                "            <field name=\"payment[cc_number]\" type=\"text\" label=\"Credit Card Number\" required=\"true\">\n" +
                "               <validators>\n" +
                "                  <validator relation=\"payment[cc_type]\" type=\"credit_card\" message=\"Credit card number does not match credit card type.\"/>\n" +
                "               </validators>\n" +
                "            </field>\n" +
                "            <field name=\"payment[cc_exp_month]\" type=\"select\" label=\"Expiration Date - Month\" required=\"true\">\n" +
                "               <values>\n" +
                "                  <item>\n" +
                "                     <label>01 - January</label>\n" +
                "                     <value>1</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>02 - February</label>\n" +
                "                     <value>2</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>03 - March</label>\n" +
                "                     <value>3</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>04 - April</label>\n" +
                "                     <value>4</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>05 - May</label>\n" +
                "                     <value>5</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>06 - June</label>\n" +
                "                     <value>6</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>07 - July</label>\n" +
                "                     <value>7</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>08 - August</label>\n" +
                "                     <value>8</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>09 - September</label>\n" +
                "                     <value>9</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>10 - October</label>\n" +
                "                     <value>10</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>11 - November</label>\n" +
                "                     <value>11</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>12 - December</label>\n" +
                "                     <value>12</value>\n" +
                "                  </item>\n" +
                "               </values>\n" +
                "            </field>\n" +
                "            <field name=\"payment[cc_exp_year]\" type=\"select\" label=\"Expiration Date - Year\" required=\"true\">\n" +
                "               <values>\n" +
                "                  <item>\n" +
                "                     <label>2017</label>\n" +
                "                     <value>2017</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2018</label>\n" +
                "                     <value>2018</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2019</label>\n" +
                "                     <value>2019</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2020</label>\n" +
                "                     <value>2020</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2021</label>\n" +
                "                     <value>2021</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2022</label>\n" +
                "                     <value>2022</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2023</label>\n" +
                "                     <value>2023</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2024</label>\n" +
                "                     <value>2024</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2025</label>\n" +
                "                     <value>2025</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2026</label>\n" +
                "                     <value>2026</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2027</label>\n" +
                "                     <value>2027</value>\n" +
                "                  </item>\n" +
                "               </values>\n" +
                "            </field>\n" +
                "         </fieldset>\n" +
                "      </form>\n" +
                "   </method>\n" +
                "</payment_methods>\n";
    }

    protected String getPaymentMethodsResponse2() {
        return "<payment_methods>\n" +
                "   <method id=\"authorizenet\" code=\"authorizenet\" post_name=\"payment[method]\" label=\"Credit Card (Authorize.net)\">\n" +
                "      <form name=\"payment_form_authorizenet\" method=\"post\">\n" +
                "         <fieldset>\n" +
                "            <field name=\"payment[cc_type]\" type=\"select\" label=\"Credit Card Type\" required=\"true\">\n" +
                "               <values>\n" +
                "                  <item>\n" +
                "                     <label>American Express</label>\n" +
                "                     <value>AE</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>Visa</label>\n" +
                "                     <value>VI</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>MasterCard</label>\n" +
                "                     <value>MC</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>Discover</label>\n" +
                "                     <value>DI</value>\n" +
                "                  </item>\n" +
                "               </values>\n" +
                "            </field>\n" +
                "            <field name=\"payment[cc_number]\" type=\"text\" label=\"Credit Card Number\" required=\"true\">\n" +
                "               <validators>\n" +
                "                  <validator relation=\"payment[cc_type]\" type=\"credit_card\" message=\"Credit card number does not match credit card type.\"/>\n" +
                "               </validators>\n" +
                "            </field>\n" +
                "            <field name=\"payment[cc_exp_month]\" type=\"select\" label=\"Expiration Date - Month\" required=\"true\">\n" +
                "               <values>\n" +
                "                  <item>\n" +
                "                     <label>01 - January</label>\n" +
                "                     <value>1</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>02 - February</label>\n" +
                "                     <value>2</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>03 - March</label>\n" +
                "                     <value>3</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>04 - April</label>\n" +
                "                     <value>4</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>05 - May</label>\n" +
                "                     <value>5</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>06 - June</label>\n" +
                "                     <value>6</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>07 - July</label>\n" +
                "                     <value>7</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>08 - August</label>\n" +
                "                     <value>8</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>09 - September</label>\n" +
                "                     <value>9</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>10 - October</label>\n" +
                "                     <value>10</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>11 - November</label>\n" +
                "                     <value>11</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>12 - December</label>\n" +
                "                     <value>12</value>\n" +
                "                  </item>\n" +
                "               </values>\n" +
                "            </field>\n" +
                "            <field name=\"payment[cc_exp_year]\" type=\"select\" label=\"Expiration Date - Year\" required=\"true\">\n" +
                "               <values>\n" +
                "                  <item>\n" +
                "                     <label>2017</label>\n" +
                "                     <value>2017</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2018</label>\n" +
                "                     <value>2018</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2019</label>\n" +
                "                     <value>2019</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2020</label>\n" +
                "                     <value>2020</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2021</label>\n" +
                "                     <value>2021</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2022</label>\n" +
                "                     <value>2022</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2023</label>\n" +
                "                     <value>2023</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2024</label>\n" +
                "                     <value>2024</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2025</label>\n" +
                "                     <value>2025</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2026</label>\n" +
                "                     <value>2026</value>\n" +
                "                  </item>\n" +
                "                  <item>\n" +
                "                     <label>2027</label>\n" +
                "                     <value>2027</value>\n" +
                "                  </item>\n" +
                "               </values>\n" +
                "            </field>\n" +
                "            <field name=\"payment[cc_cid]\" type=\"text\" label=\"Card Verification Number\" required=\"true\">\n" +
                "               <validators>\n" +
                "                  <validator relation=\"payment[cc_type]\" type=\"credit_card_svn\" message=\"Card verification number is wrong\"/>\n" +
                "               </validators>\n" +
                "            </field>\n" +
                "         </fieldset>\n" +
                "      </form>\n" +
                "   </method>\n" +
                "</payment_methods>\n";
    }

    protected ArrayList<PaymentMethod> getPaymentMethods(String xml) {
        ArrayList<PaymentMethod> methods = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tag = parser.getName();
                    if (tag.equals("method")) {
                        String id = parser.getAttributeValue(null, "id");
                        String code = parser.getAttributeValue(null, "code");
                        String postName = parser.getAttributeValue(null, "post_name");
                        String label = parser.getAttributeValue(null, "label");
                        PaymentMethod method = new PaymentMethod();
                        method.setId(id);
                        method.setCode(code);
                        method.setPostName(postName);
                        method.setLabel(label);
                        Form form = this.getPaymentMethodForm(parser, eventType);
                        method.setForm(form);
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

    protected Form getPaymentMethodForm(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        Form form = new Form();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("method")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("form")) {
                    String name = parser.getAttributeValue(null, "name");
                    String formMethod = parser.getAttributeValue(null, "method");
                    form.setName(name);
                    form.setMethod(formMethod);
                    List<FormField> fields = this.getPaymentMethodFormFields(parser, eventType);
                    form.setFields(fields);
                }
            }
            eventType = parser.next();
        }
        return form;
    }

    protected List<FormField> getPaymentMethodFormFields(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        List<FormField> fields = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("form")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("field")) {
                    String name = parser.getAttributeValue(null, "name");
                    String type = parser.getAttributeValue(null, "type");
                    String label = parser.getAttributeValue(null, "label");
                    String required = parser.getAttributeValue(null, "required");
                    FormField field = new FormField();
                    field.setName(name);
                    field.setType(type);
                    field.setLabel(label);
                    field.setRequired(required);
                    this.parseFormFieldItems(parser, eventType, field);
                    fields.add(field);
                }
            }
            eventType = parser.next();
        }
        return fields;
    }

    protected void parseFormFieldItems(XmlPullParser parser, int eventType, FormField field)
            throws IOException, XmlPullParserException {
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("field")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("validators")) {
                    List<FormFieldValidator> validators =
                            this.getFormFieldValidators(parser, eventType);
                    field.setValidators(validators);
                } else if (tag.equals("values")) {
                    List<FormFieldValue> vals = this.getFormFieldValues(parser, eventType);
                    field.setValues(vals);
                }
            }
            eventType = parser.next();
        }
    }

    protected List<FormFieldValidator> getFormFieldValidators(XmlPullParser parser, int eventType)
            throws XmlPullParserException, IOException {
        List<FormFieldValidator> validators = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("validators")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("validator")) {
                    String relation = parser.getAttributeValue(null, "relation");
                    String type = parser.getAttributeValue(null, "type");
                    String message = parser.getAttributeValue(null, "message");
                    FormFieldValidator validator = new FormFieldValidator();
                    validator.setRelation(relation);
                    validator.setType(type);
                    validator.setMessage(message);
                    validators.add(validator);
                }
            }
            eventType = parser.next();
        }
        return validators;
    }

    protected List<FormFieldValue> getFormFieldValues(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        List<FormFieldValue> vals = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("values")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("item")) {
                    FormFieldValue val = this.getFormFieldValue(parser, eventType);
                    vals.add(val);
                }
            }
            eventType = parser.next();
        }
        return vals;
    }

    protected FormFieldValue getFormFieldValue(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        FormFieldValue val = new FormFieldValue();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("item")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("label")) {
                    val.setLabel(parser.nextText());
                } else if (tag.equals("value")) {
                    val.setValue(parser.nextText());
                }
            }
            eventType = parser.next();
        }
        return val;
    }

    public ResponseMessage savePayment(RequestParamList data) {
        mPath = "xmlconnect/checkout/savePayment";
        mParams = data;
        Log.d(TAG, "post data: " + data);
        String url = this.getRequestUrl();
        String resp = this.getContentByUrl(url);
        ResponseMessage msg = this.parseResponseMessage(resp);
        return msg;
    }
}
