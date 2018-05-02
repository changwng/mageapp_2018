package com.example.mageapp.xmlconnect;

import android.content.Context;
import android.util.Log;

import com.example.mageapp.catalog.Category;
import com.example.mageapp.catalog.Product;
import com.example.mageapp.helper.Cookie;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 4/2/17.
 */

public class CategoryConntect extends DefaultConnect {

    protected static final int PROD_CNT = 100;
    protected List<Category> mSubCats = new ArrayList<>();
    protected List<Product> mProducts = new ArrayList<>();

    public CategoryConntect(Context context) {
        super(context);
        mPath = "xmlconnect/catalog/category";
    }

    public Category fetchCategoryInfoById(String id) {
        Category c = new Category();
        c.setId(id);
        String url = getRequestUrl();
        setPostData("id", id);
        setPostData("count", String.valueOf(PROD_CNT));
        String xml = getContentByUrl(url);
        try {
            parseXml(xml);
            if (!mSubCats.isEmpty()) {
                c.setChildren(mSubCats);
            }
            if (!mProducts.isEmpty()) {
                c.setProducts(mProducts);
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return c;
    }

    public void parseXml(String xml) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xml));
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            // sub category items
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals("items")) {
                    setSubCategories(parser, eventType);
                } else if (tag.equals("products")) {
                    setProducts(parser, eventType);
                } else if (tag.equals("message")) {
                    ResponseMessage msg = this.parseResponseMessage(xml);
                    if (!msg.isSuccess()) { // remove session cookie if it's bad
                        Cookie.getInstance(mContext).removeHeaderCookie();
                    }
                }
            }
            eventType = parser.next();
        }
    }

    public void setSubCategories(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        while (!((eventType == XmlPullParser.END_TAG) && parser.getName().equals("items"))) {
            if ((eventType == XmlPullParser.START_TAG) && parser.getName().equals("item")) {
                Category c = getSubCategory(parser, eventType);
                mSubCats.add(c);
            }
            eventType = parser.next();
        }
    }

    public Category getSubCategory(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        Category c = new Category();
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("item")))) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if ("label".equals(name)) {
                    c.setLabel(parser.nextText());
                } else if ("entity_id".equals(name)) {
                    c.setId(parser.nextText());
                } else if ("icon".equals(name)) {
                    c.setIcon(parser.nextText());
                }
            }
            eventType = parser.next();
        }
        return c;
    }

    public void setProducts(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        while (!((eventType == XmlPullParser.END_TAG) && (parser.getName().equals("products")))) {
            if ((eventType == XmlPullParser.START_TAG) && (parser.getName().equals("item"))) {
                Product p = getProduct(parser, eventType);
                mProducts.add(p);
            }
            eventType = parser.next();
        }
    }

    public Product getProduct(XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        Product p = new Product();
        while (!((eventType == XmlPullParser.END_TAG) && ("item".equals(parser.getName())))) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if ("entity_id".equals(name)) {
                    p.setId(parser.nextText());
                } else if ("name".equals(name)) {
                    p.setName(parser.nextText());
                } else if ("link".equals(name)) {
                    p.setLink(parser.nextText());
                } else if ("icon".equals(name)) {
                    p.setIcon(parser.nextText());
                } else if ("price".equals(name)) {
                    String reqular = parser.getAttributeValue(null, "regular");
                    p.setPrice(reqular);
                    String special = parser.getAttributeValue(null, "special");
                    p.setSpecialPrice(special);
                }
            }
            eventType = parser.next();
        }
        return p;
    }
}