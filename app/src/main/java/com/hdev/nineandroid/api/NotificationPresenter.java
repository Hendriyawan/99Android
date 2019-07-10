package com.hdev.nineandroid.api;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.hdev.nineandroid.interfaces.NotificationView;
import com.hdev.nineandroid.utils.AppPreferences;

public class NotificationPresenter {
    private Context context;
    private NotificationView view;

    public NotificationPresenter(Context context, NotificationView view) {
        this.context = context;
        this.view = view;
    }

    public void laodNotification() {
        if (AppPreferences.getFirstInstall(context)) {
            AndroidNetworking.get(EndPoint.URL)
                    .setPriority(Priority.LOW)
                    .setTag("NOTIFICATION")
                    .build()
                    .getAsObject(NotificationResponse.class, new ParsedRequestListener<NotificationResponse>() {
                        @Override
                        public void onResponse(NotificationResponse response) {
                            Log.d("DEBUG", response.getMessage());
                            if (response.getSuccess()) {
                                view.onNotificationFirstLoaded(response.getNotification());
                            } else {
                                view.onDataEmpty();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            view.onDataEmpty();
                        }
                    });
        }
    }
}
