package com.example.mageapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mageapp.db.LogDbContract.LogEntry;

/**
 * Created by foo on 5/29/17.
 */

public class LogDbHelper extends SQLiteOpenHelper {

    protected static final String DB_NAME = "log.db";
    protected static final int DB_VER = 1;
    protected static final String SQL_CREATE_LOG = "CREATE TABLE " + LogEntry.TABLE_NAME + " ( " +
            LogEntry._ID + " INT PRIMARY KEY, " +
            LogEntry.COLUMN_NAME_DATA + "  TEXT);";

    public LogDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
