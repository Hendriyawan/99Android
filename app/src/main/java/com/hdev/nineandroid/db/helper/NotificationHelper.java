package com.hdev.nineandroid.db.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.hdev.nineandroid.db.model.Notifications;
import com.hdev.nineandroid.interfaces.NotificationView;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.hdev.nineandroid.db.contract.NotificationContract.NotificationColumns.BODY;
import static com.hdev.nineandroid.db.contract.NotificationContract.NotificationColumns.DATE;
import static com.hdev.nineandroid.db.contract.NotificationContract.NotificationColumns.STATUS_READ;
import static com.hdev.nineandroid.db.contract.NotificationContract.NotificationColumns.TITLE;
import static com.hdev.nineandroid.db.contract.NotificationContract.TABLE_NAME;

public class NotificationHelper {
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public NotificationHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    @SuppressLint("StaticFieldLeak")
    public void getAllNotification(final NotificationView view) {
        new AsyncTask<Void, Void, List<Notifications>>() {
            @Override
            protected List<Notifications> doInBackground(Void... voids) {
                return queryGetAllNotification();
            }

            @Override
            protected void onPostExecute(List<Notifications> notifications) {
                super.onPostExecute(notifications);
                if (notifications.size() > 0) {
                    view.onNotificationLoaded(notifications);
                } else {
                    view.onDataEmpty();
                    //view.onNotificationLoaded(notifications);
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void getCount(final TextView textViewCount) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                Log.d("DEBUG_COUNT", String.valueOf(getCount()));
                return getCount();
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                textViewCount.setText(String.valueOf(integer));

            }
        }.execute();
    }

    private int getCount() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + STATUS_READ + " LIKE '%"+"unread"+"%'", null);
        cursor.moveToFirst();
        int result = cursor.getCount();
        cursor.close();
        return result;
    }

    @SuppressLint("StaticFieldLeak")
    public void insert(final Notifications notifications) {
        Log.d("DEBUG", "insert executed()");
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                ContentValues cv = new ContentValues();
                cv.put(TITLE, notifications.getTitle());
                cv.put(BODY, notifications.getBody());
                cv.put(DATE, notifications.getDate());
                cv.put(STATUS_READ, notifications.getStatusRead());
                return database.insert(TABLE_NAME, null, cv);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                Log.d("DEBUG_INSERT", String.valueOf(aLong));
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void update(final Notifications notifications, final String id) {
        Log.d("DEBUG", "update executed");
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                ContentValues cv = new ContentValues();
                cv.put(TITLE, notifications.getTitle());
                cv.put(BODY, notifications.getBody());
                cv.put(DATE, notifications.getDate());
                cv.put(STATUS_READ, notifications.getStatusRead());
                return database.update(TABLE_NAME, cv, _ID + " = ?", new String[]{id});
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.d("DEBUG_UPDATE", String.valueOf(integer));
            }
        }.execute();
    }


    //query get All notification
    private List<Notifications> queryGetAllNotification() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + _ID + " DESC", null);
        cursor.moveToFirst();

        List<Notifications> notificationsList = new ArrayList<>();
        Notifications notifications;
        if (cursor.getCount() > 0) {
            do {
                notifications = new Notifications();
                notifications.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                notifications.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                notifications.setBody(cursor.getString(cursor.getColumnIndexOrThrow(BODY)));
                notifications.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                notifications.setStatusRead(cursor.getString(cursor.getColumnIndexOrThrow(STATUS_READ)));
                notificationsList.add(notifications);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return notificationsList;
    }
}