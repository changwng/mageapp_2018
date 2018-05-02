package com.example.mageapp.xmlconnect;

/**
 * Created by foo on 9/3/17.
 */

public class ResponseMessage {

    public static final String STATUS_SUCCESS = "success";

    protected String mStatus;
    protected String mText;
    protected boolean mSuccess;

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public boolean isSuccess() {
        return mStatus.equals(STATUS_SUCCESS);
    }
}
