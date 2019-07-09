package com.hdev.nineandroid.fcm;

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
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hdev.nineandroid.R;
import com.hdev.nineandroid.db.helper.NotificationHelper;
import com.hdev.nineandroid.db.model.Notifications;
import com.hdev.nineandroid.view.NotificationHistoryActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FirebaseCloudMessageService extends FirebaseMessagingService {
    String TAG = FirebaseCloudMessageService.class.getSimpleName();
    private NotificationHelper notificationHelper;

    @Override
    public void onCreate() {
        notificationHelper = new NotificationHelper(this);
        notificationHelper.open();
    }

    public void subscribe(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
        Log.d(TAG, "subscribed : " + topic);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken : Refreshed Token = " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            handleDebug("NOTIFICATION", remoteMessage);
            sendBroadcast(remoteMessage.getNotification().getTitle());
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            saveNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData() != null) {
            handleDebug("DATA", remoteMessage);
            sendBroadcast(remoteMessage.getData().get("title"));
            showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
            saveNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));

        }
    }

    /*
    show Notification
     */
    private void showNotification(String title, String body) {
        Log.d("DEBUG", "showNotification");

        String channel_id = getString(R.string.default_notification_channel_id);
        String channel_name = getString(R.string.default_notification_channel_name);

        Intent notificationHistoryChannelIntent = new Intent(this, NotificationHistoryActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationHistoryChannelIntent, PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                channel_id)
                .setSmallIcon(R.drawable.ic_notification_active)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(uri);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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


    /*
    save data to sqlite database
     */
    private void saveNotification(String title, String body) {
        Log.d("DEBUG", "insert");
        String date = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS", Locale.getDefault()).format(new Date().getTime());
        Notifications notifications = new Notifications();
        notifications.setTitle(title);
        notifications.setBody(body);
        notifications.setDate(date);
        notifications.setStatusRead("unread");
        notificationHelper.insert(notifications);
    }

    /*
    send Broadscast
     */
    private void sendBroadcast(String title){
        Intent intent = new Intent("NOTIFICATION_VALUE");
        intent.putExtra("TITLE", title);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /*
    handle debug
     */
    private void handleDebug(String type, RemoteMessage remoteMessage) {
        Log.d("DEBUG", type);
        if (type.equals("DATA")) {
            Log.d("DEBUG", remoteMessage.getData().get("title"));
            Log.d("DEBUG", remoteMessage.getData().get("body"));
        } else {
            Log.d("DEBUG", remoteMessage.getNotification().getTitle());
            Log.d("DEBUG", remoteMessage.getNotification().getBody());
        }
    }
}