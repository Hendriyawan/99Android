package com.hdev.nineandroid.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.hdev.nineandroid.db.contract.NotificationContract.NotificationColumns.BODY;
import static com.hdev.nineandroid.db.contract.NotificationContract.NotificationColumns.DATE;
import static com.hdev.nineandroid.db.contract.NotificationContract.NotificationColumns.STATUS_READ;
import static com.hdev.nineandroid.db.contract.NotificationContract.NotificationColumns.TITLE;
import static com.hdev.nineandroid.db.contract.NotificationContract.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "db_notification";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY, " +
            TITLE + " TEXT NOT NULL, " +
            BODY + " TEXT NOT NULL, " +
            DATE + " TEXT NOT NULL, " +
            STATUS_READ + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
