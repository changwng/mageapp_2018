package com.example.mageapp.helper;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by foo on 3/18/17.
 */

public class Helper {

    protected static final String TAG = "Helper";
    protected static final String DOMAIN = "mage.testing.acacloud.com";
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    public static final String UTF_8 = "UTF-8";
    protected static final String LOG_FILE = "debug.log";
    public static final int IMAGE_SCALE_VALUE = 3;
    protected static Helper sInstance;
    protected Context mContext;

    protected Helper(Context context) {
        mContext = context;
    }

    public static Helper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Helper(context);
        }
        return sInstance;
    }

    public void fileLog(String msg) {
        File file = new File(mContext.getFilesDir(), LOG_FILE);
        String filePath = file.getPath();
        Log.d(TAG, "filePath: " + filePath);
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(mContext.openFileOutput(LOG_FILE, Context.MODE_APPEND));
            byte[] bytes = msg.getBytes();
            out.write(bytes);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Failed to find the file: " + LOG_FILE, e);
        } catch (IOException e) {
            Log.e(TAG, msg, e);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }

    public long dbLog(String msg) {
        return Db.getInstance(mContext).addLog(msg);
    }

    public String getLogs() {
        String s = null;
        List<String> logs = Db.getInstance(mContext).getLogs();
        int numLogs = logs.size();
        for (int i = 0; i < numLogs; i++) {
            s += logs.get(i);
        }
        return s;
    }

    public static String getGMT(Date date) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String gmt = sdf.format(date);
        return gmt;
    }

    public static DisplayMetrics getScreenSize(Context context) {
        WindowManager winMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        winMgr.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }

    public static byte[] getUrlBytes(String url) {
        try {
            return getBytesByUrl(url);
        } catch (MalformedURLException me) {
            Log.e(TAG, me.getMessage(), me);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage(), ioe);
        }
        return null;
    }

    protected static byte[] getBytesByUrl(String spec) throws IOException {
        URL url = new URL(spec);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return null;
        }
        InputStream in = new BufferedInputStream(conn.getInputStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(bytes)) != -1) {
            out.write(bytes, 0, bytesRead);
        }
        conn.disconnect();
        in.close();
        out.close();
        return out.toByteArray();
    }

    public static String getDomain() {
        return DOMAIN;
    }

    public static int generateViewId() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            return View.generateViewId();
        }
        return getGenerateViewId();
    }

    protected static int getGenerateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}