package com.example.mageapp.xmlconnect;

import android.content.Context;
import android.util.Log;

import com.example.mageapp.cart.CartTotal;
import com.example.mageapp.checkout.OrderReview;
import com.example.mageapp.helper.RequestParamList;
import com.example.mageapp.sales.QuoteItem;
import com.example.mageapp.sales.QuoteItemOption;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 10/19/17.
 */

public class CheckoutReviewConnect extends DefaultConnect {

    protected static final String TAG = CheckoutReviewConnect.class.getSimpleName();

    public CheckoutReviewConnect(Context context) {
        super(context);
    }

    public OrderReview fetccOrderReview() {
        mPath = "xmlconnect/checkout/orderReview";
        String url = this.getRequestUrl();
        String resp = this.getContentByUrl(url);
//        String resp = this.getOrderReviewResponse();
        return this.getOrderReview(resp);
    }

    protected String getOrderReviewResponse() {
        return "<order>\n" +
                "   <products>\n" +
                "      <item>\n" +
                "         <entity_id>410</entity_id>\n" +
                "         <entity_type>configurable</entity_type>\n" +
                "         <item_id>2931</item_id>\n" +
                "         <name>Chelsea Tee</name>\n" +
                "         <qty>1</qty>\n" +
                "         <icon modification_time=\"1505256575\">http://mage.testing.acacloud.com/media/catalog/product/cache/1/thumbnail/158x/9df78eab33525d08d6e5fb8d27136e95/m/t/mtk004t.jpg</icon>\n" +
                "         <price regular=\"75.00\"/>\n" +
                "         <formated_price regular=\"$75.00\"/>\n" +
                "         <subtotal regular=\"75.00\"/>\n" +
                "         <formated_subtotal regular=\"$75.00\"/>\n" +
                "         <options>\n" +
                "            <option label=\"Color\" text=\"Blue\"/>\n" +
                "            <option label=\"Size\" text=\"M\"/>\n" +
                "         </options>\n" +
                "      </item>\n" +
                "      <item>\n" +
                "         <entity_id>337</entity_id>\n" +
                "         <entity_type>simple</entity_type>\n" +
                "         <item_id>2933</item_id>\n" +
                "         <name>Aviator Sunglasses</name>\n" +
                "         <qty>1</qty>\n" +
                "         <icon modification_time=\"1504663525\">http://mage.testing.acacloud.com/media/catalog/product/cache/1/thumbnail/158x/9df78eab33525d08d6e5fb8d27136e95/a/c/ace000a_1.jpg</icon>\n" +
                "         <price regular=\"295.00\"/>\n" +
                "         <formated_price regular=\"$295.00\"/>\n" +
                "         <subtotal regular=\"295.00\"/>\n" +
                "         <formated_subtotal regular=\"$295.00\"/>\n" +
                "      </item>\n" +
                "      <item>\n" +
                "         <entity_id>417</entity_id>\n" +
                "         <entity_type>configurable</entity_type>\n" +
                "         <item_id>2934</item_id>\n" +
                "         <name>NoLIta Cami</name>\n" +
                "         <qty>1</qty>\n" +
                "         <icon modification_time=\"1505433387\">http://mage.testing.acacloud.com/media/catalog/product/cache/1/thumbnail/158x/9df78eab33525d08d6e5fb8d27136e95/w/b/wbk000t.jpg</icon>\n" +
                "         <price regular=\"150.00\"/>\n" +
                "         <formated_price regular=\"$150.00\"/>\n" +
                "         <subtotal regular=\"150.00\"/>\n" +
                "         <formated_subtotal regular=\"$150.00\"/>\n" +
                "         <options>\n" +
                "            <option label=\"Color\" text=\"Pink\"/>\n" +
                "            <option label=\"Size\" text=\"S\"/>\n" +
                "         </options>\n" +
                "      </item>\n" +
                "   </products>\n" +
                "   <totals>\n" +
                "      <subtotal>\n" +
                "         <title>Subtotal</title>\n" +
                "         <value>520.00</value>\n" +
                "         <formated_value>$520.00</formated_value>\n" +
                "      </subtotal>\n" +
                "      <shipping>\n" +
                "         <title>Shipping &amp; Handling (United Parcel Service - 3 Day Select)</title>\n" +
                "         <value>36.41</value>\n" +
                "         <formated_value>$36.41</formated_value>\n" +
                "      </shipping>\n" +
                "      <tax>\n" +
                "         <title>Tax</title>\n" +
                "         <value>42.90</value>\n" +
                "         <formated_value>$42.90</formated_value>\n" +
                "      </tax>\n" +
                "      <grand_total>\n" +
                "         <title>Grand Total</title>\n" +
                "         <value>556.41</value>\n" +
                "         <formated_value>$556.41</formated_value>\n" +
                "      </grand_total>\n" +
                "   </totals>\n" +
                "   <agreements/>\n" +
                "</order>";
    }

    protected OrderReview getOrderReview(String xml) {
        OrderReview review = new OrderReview();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tag = parser.getName();
                    if (tag.equals("products")) {
                        List<QuoteItem> items = this.getQuoteItems(parser, eventType);
                        review.setProducts(items);
                    } else if (tag.equals("totals")) {
                        List<CartTotal> totals = this.getQuoteTotals(parser, eventType);
                        review.setTotals(totals);
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, xppe.getMessage(), xppe);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage(), ioe);
        }
        return review;
    }

    protected List<QuoteItem> getQuoteItems(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        List<QuoteItem> items = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("products")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("item")) {
                    QuoteItem item = this.getQuoteItem(parser, eventType);
                    items.add(item);
                }
            }
            eventType = parser.next();
        }
        return items;
    }

    protected List<CartTotal> getQuoteTotals(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        List<CartTotal> totals = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("totals")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                switch (tag) {
                    case "subtotal":
                    case "shipping":
                    case "tax":
                    case "grand_total":
                        CartTotal total = this.getCartTotal(parser, eventType, tag);
                        totals.add(total);
                        break;
                }
            }
            eventType = parser.next();
        }
        return totals;
    }

    protected QuoteItem getQuoteItem(XmlPullParser parser, int eventType) throws IOException,
            XmlPullParserException {
        QuoteItem item = new QuoteItem();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("item")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("entity_id")) {
                    String entityId = parser.nextText();
                    item.setEntityId(entityId);
                } else if (tag.equals("entity_type"))  {
                    String entityType = parser.nextText();
                    item.setEntityType(entityType);
                } else if (tag.equals("item_id")) {
                    String itemId = parser.nextText();
                    item.setItemId(itemId);
                } else if (tag.equals("name")) {
                    String name = parser.nextText();
                    item.setName(name);
                } else if (tag.equals("qty")) {
                    String qty = parser.nextText();
                    item.setQty(qty);
                } else if (tag.equals("icon")) {
                    String icon = parser.nextText();
                    item.setIcon(icon);
                } else if (tag.equals("price")) {
                    String price = parser.getAttributeValue(null, "regular");
                    item.setPrice(price);
                } else if (tag.equals("formated_price")) {
                    String formatedPrice = parser.getAttributeValue(null, "regular");
                    item.setFormatedPrice(formatedPrice);
                } else if (tag.equals("subtotal")) {
                    String subtotal = parser.getAttributeValue(null, "regular");
                    item.setSubtotal(subtotal);
                } else if (tag.equals("formated_subtotal")) {
                    String formatedSubtotal = parser.getAttributeValue(null, "regular");
                    item.setFormatedSubtotal(formatedSubtotal);
                } else if (tag.equals("options")) {
                    List<QuoteItemOption> options = this.getQuoteItemOptions(parser, eventType);
                    item.setOptions(options);
                }
            }
            eventType = parser.next();
        }
        return item;
    }

    protected CartTotal getCartTotal(XmlPullParser parser, int eventType, String totalType)
            throws IOException, XmlPullParserException {
        CartTotal total = new CartTotal();
        total.setType(totalType);
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals(totalType)))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("title")) {
                    String title = parser.nextText();
                    total.setTitle(title);
                } else if (tag.equals("value")) {
                    String value = parser.nextText();
                    total.setValue(value);
                } else if (tag.equals("formated_value")) {
                    String formatedValue = parser.nextText();
                    total.setFormatedValue(formatedValue);
                }
            }
            eventType = parser.next();
        }
        return total;
    }

    protected List<QuoteItemOption> getQuoteItemOptions(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        List<QuoteItemOption> options = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("options")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("option")) {
                    String label = parser.getAttributeValue(null, "label");
                    String text = parser.getAttributeValue(null, "text");
                    QuoteItemOption option = new QuoteItemOption();
                    option.setLabel(label);
                    option.setText(text);
                    options.add(option);
                }
            }
            eventType = parser.next();
        }
        return options;
    }

    public ResponseMessage placeOrder(RequestParamList data) {
        mPath = "xmlconnect/checkout/saveOrder";
        mParams = data;
        String url = this.getRequestUrl();
        String resp = this.getContentByUrl(url);
//        String resp = this.getPlaceOrderResponse();
//        ResponseMessage respMsg = this.parseResponseMessage(resp);
//        return respMsg;
        return mRespMsg;
    }

    protected String getPlaceOrderResponse() {
        return "<message>\n" +
                "   <status>success</status>\n" +
                "   <text>Thank you for your purchase! Your order # is: 145000009. You will receive an order confirmation email with details of your order and a link to track its progress.</text>\n" +
                "   <order_id>145000009</order_id>\n" +
                "</message>";
    }
}