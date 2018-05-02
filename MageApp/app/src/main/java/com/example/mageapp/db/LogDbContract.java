package com.example.mageapp.db;

import android.provider.BaseColumns;

/**
 * Created by foo on 5/29/17.
 */

public final class LogDbContract {

    private LogDbContract() {
    }

    public static class LogEntry implements BaseColumns {
        public static final String TABLE_NAME = "log";
        public static final String COLUMN_NAME_DATA = "data";
    }
}
