package com.example.mageapp.helper;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by foo on 8/27/17.
 */

public class Test {

    protected static final String TAG = Test.class.getSimpleName();

    public String getUTC(Date date) {
        /*Calendar cal = Calendar.getInstance();
        TimeZone tzone = TimeZone.getTimeZone("GMT");
        cal.setTimeZone(tzone);
        Date date = cal.getTime();
        return date.toString();*/

        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String gmt = sdf.format(date);
        return gmt;
    }

    public void checkBoolean() {
        String required = null;
        boolean val = ((required != null) && (required.equals("required")));

        Log.d(TAG, "val: " + val);
        return;
    }

    public void checkForLoop() {
        int n = 0;
        for (int i = 0; i < 10; i++) {
            if (i == 2) {
                n = i;
                break;
            }
        }
        Log.d(TAG, "n: " + n);
        return;
    }
}
