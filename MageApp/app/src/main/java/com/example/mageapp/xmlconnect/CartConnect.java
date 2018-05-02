package com.example.mageapp.xmlconnect;

import android.content.Context;
import android.util.Log;

import com.example.mageapp.cart.Cart;
import com.example.mageapp.cart.CartData;
import com.example.mageapp.cart.CartInfo;
import com.example.mageapp.cart.CartItem;
import com.example.mageapp.cart.CartItemOption;
import com.example.mageapp.cart.CartTotal;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 3/18/17.
 */

public class CartConnect extends DefaultConnect {

    public CartConnect(Context context) {
        super(context);
        mPath = "xmlconnect/cart/index";
    }

    public ResponseMessage addItemToCart(CartData data) {
        mPath = "xmlconnect/cart/add";
        setPostData("qty", "1");
        setPostData("product", data.getProductId());
        for (String key : data.getOptions().keySet()) {
            String val = data.getOptions().get(key);
            setPostData(key, val);
        }
        String url = getRequestUrl();
        String xml = getContentByUrl(url);
        return parseResponseMessage(xml);
    }

    public Cart fetchCart() {
        Cart cart = new Cart();
        String url = getRequestUrl();
        String xml = getContentByUrl(url);
        try {
            cart = parseCartXml(xml);
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, xppe.getMessage(), xppe);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage(), ioe);
        }
        return cart;
    }

    protected Cart parseCartXml(String xml) throws XmlPullParserException, IOException {
        Cart cart = new Cart();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xml));
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_TAG) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (name.equals("cart")) {
                    String summaryQty = parser.getAttributeValue(null, "summary_qty");
                    cart.setSummaryQty(summaryQty);
                } else if (name.equals("products")) {
                    List<CartItem> items = getCartItems(parser, eventType);
                    cart.setItems(items);
                }
            }
            eventType = parser.next();
        }
        return cart;
    }

    protected List<CartItem> getCartItems(XmlPullParser parser, int eventType) throws IOException,
            XmlPullParserException {
        List<CartItem> items = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && parser.getName().equals("products"))) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (name.equals("item")) {
                    CartItem item = getCartItem(parser, eventType);
                    items.add(item);
                }
            }
            eventType = parser.next();
        }
        return items;
    }

    protected CartItem getCartItem(XmlPullParser parser, int eventType) throws IOException,
            XmlPullParserException {
        CartItem item = new CartItem();
        while (!((eventType == XmlPullParser.END_TAG) && parser.getName().equals("item"))) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (name.equals("entity_id")) {
                    item.setEntityId(parser.nextText());
                } else if (name.equals("entity_type")) {
                    item.setEntityType(parser.nextText());
                } else if (name.equals("item_id")) {
                    item.setItemId(parser.nextText());
                } else if (name.equals("name")) {
                    item.setName(parser.nextText());
                } else if (name.equals("code")) {
                    item.setCode(parser.nextText());
                } else if (name.equals("qty")) {
                    item.setQty(parser.nextText());
                } else if (name.equals("icon")) {
                    item.setIcon(parser.nextText());
                } else if (name.equals("price")) {
                    item.setPrice(parser.getAttributeValue(null, "regular"));
                } else if (name.equals("formated_price")) {
                    item.setFormatedPrice(parser.getAttributeValue(null, "regular"));
                } else if (name.equals("subtotal")) {
                    item.setSubtotal(parser.getAttributeValue(null, "regular"));
                } else if (name.equals("formated_subtotal")) {
                    item.setFormatedSubtotal(parser.getAttributeValue(null, "regular"));
                } else if (name.equals("options")) {
                    List<CartItemOption> options = getCartItemOptions(parser, eventType);
                    item.setOptions(options);
                }
            }
            eventType = parser.next();
        }
        return item;
    }

    protected List<CartItemOption> getCartItemOptions(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        List<CartItemOption> options = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && parser.getName().equals("options"))) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (name.equals("option")) {
                    String label = parser.getAttributeValue(null, "label");
                    String text = parser.getAttributeValue(null, "text");
                    CartItemOption option = new CartItemOption();
                    option.setLabel(label);
                    option.setText(text);
                    options.add(option);
                }
            }
            eventType = parser.next();
        }
        return options;
    }

    public CartInfo fetchCartInfo() {
        mPath = "xmlconnect/cart/info";
        String url = this.getRequestUrl();
        String resp = this.getContentByUrl(url);
        CartInfo info = parseCartInfo(resp);
        return info;
    }

    protected CartInfo parseCartInfo(String xml) {
        CartInfo info = new CartInfo();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tag = parser.getName();
                    if (tag.equals("is_virtual")) {
                        info.setIsVirtual(parser.nextText());
                    } else if (tag.equals("summary_qty")) {
                        info.setSummaryQty(parser.nextText());
                    } else if (tag.equals("virtual_qty")) {
                        info.setVirtualQty(parser.nextText());
                    } else if (tag.equals("totals")) {
                        List<CartTotal> totals = parseCartTotals(parser, eventType);
                        info.setTotals(totals);
                    }
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException xppe) {
            Log.e(TAG, xppe.getMessage(), xppe);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage(), ioe);
        }
        return info;
    }

    protected List<CartTotal> parseCartTotals(XmlPullParser parser, int eventType)
            throws XmlPullParserException, IOException {
        List<CartTotal> totals = new ArrayList<>();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("totals")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("subtotal")) {
                    CartTotal total = parseCartTotal(parser, eventType, "subtotal");
                    totals.add(total);
                } else if (tag.equals("tax")) {
                    CartTotal total = parseCartTotal(parser, eventType, "tax");
                    totals.add(total);
                } else if (tag.equals("grand_total")) {
                    CartTotal total = parseCartTotal(parser, eventType, "grand_total");
                    totals.add(total);
                }
            }
            eventType = parser.next();
        }
        return totals;
    }

    protected CartTotal parseCartTotal(XmlPullParser parser, int eventType, String endTag)
            throws XmlPullParserException, IOException {
        CartTotal total = new CartTotal();
        total.setType(endTag);
        String name = parser.getName();
        while (!(eventType == XmlPullParser.END_TAG) && (name.equals(endTag))) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("title")) {
                    total.setTitle(parser.nextText());
                } else if (tag.equals("value")) {
                    total.setValue(parser.nextText());
                } else if (tag.equals("formated_value")) {
                    total.setFormatedValue(parser.nextText());
                }
            }
            eventType = parser.next();
        }
        return total;
    }

    public ResponseMessage removeItem(String itemId) {
        mPath = "xmlconnect/cart/delete";
        setPostData("item_id", itemId);
        String url = this.getRequestUrl();
        this.getContentByUrl(url);
        return mRespMsg;
    }
}
