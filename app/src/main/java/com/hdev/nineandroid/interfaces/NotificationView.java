package com.hdev.nineandroid.interfaces;

import com.hdev.nineandroid.db.model.Notifications;

import java.util.List;

public interface NotificationView {
    void onNotificationLoaded(List<Notifications> notifications);

    void onDataEmpty();
}
