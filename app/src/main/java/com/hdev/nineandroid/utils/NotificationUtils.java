package com.hdev.nineandroid.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.hdev.nineandroid.R;
import com.hdev.nineandroid.view.NotificationHistoryActivity;

public class NotificationUtils {
    public static void show(Context context, String title, String body) {
        String channel_id = context.getResources().getString(R.string.default_notification_channel_id);
        String channel_name = context.getResources().getString(R.string.default_notification_channel_name);

        Intent notificationHistoryIntent = new Intent(context, NotificationHistoryActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationHistoryIntent, PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                channel_id)
                .setSmallIcon(R.drawable.ic_notification_active)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(uri);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //check sdk version
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(channel_id);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        Notification notification = builder.build();
        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }

    }
}
