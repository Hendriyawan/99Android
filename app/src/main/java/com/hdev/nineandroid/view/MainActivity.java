package com.hdev.nineandroid.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hdev.nineandroid.R;
import com.hdev.nineandroid.api.NotificationPresenter;
import com.hdev.nineandroid.db.helper.NotificationHelper;
import com.hdev.nineandroid.db.model.Notifications;
import com.hdev.nineandroid.fcm.FirebaseCloudMessageService;
import com.hdev.nineandroid.interfaces.NotificationView;
import com.hdev.nineandroid.utils.AppPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NotificationView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_count_notification)
    TextView textViewCountNotification;
    @BindView(R.id.progressbar_horizontal)
    ProgressBar progressBarHorizontal;
    @BindView(R.id.webview)
    WebView webView;
    private NotificationHelper notificationHelper;
    /*
    BroadcastReceiver
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(MainActivity.this, intent.getStringExtra("TITLE"), Toast.LENGTH_LONG).show();
            notificationHelper.getCount(textViewCountNotification);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initFCM();
        initToolbar();
        initProgressBar();
        initWebView();
        initDefaultNotification();
        notificationHelper = new NotificationHelper(this);
        notificationHelper.open();

    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("NOTIFICATION_VALUE"));
        notificationHelper.getCount(textViewCountNotification);
    }

    @Override
    protected void onResume() {
        super.onResume();
        notificationHelper.getCount(textViewCountNotification);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();

        } else {
            super.onBackPressed();
        }
    }

    /*
    Onclick
     */
    @OnClick(R.id.relative_layout_text_view_notification)
    public void relativeClick() {
        startActivity(new Intent(this, NotificationHistoryActivity.class));
    }

    /*
    OnClick
     */
    @OnClick(R.id.text_count_notification)
    public void textCountClick() {
        startActivity(new Intent(this, NotificationHistoryActivity.class));
    }


    /*
    init FirebaseCloudMessaging
     */
    private void initFCM() {
        FirebaseCloudMessageService firebaseCloudMessageService = new FirebaseCloudMessageService();
        firebaseCloudMessageService.subscribe("99Android");
    }

    /*
    Init Toolbar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /*
    Init ProgressBar
     */
    private void initProgressBar() {
        progressBarHorizontal.setMax(100);
        progressBarHorizontal.setProgress(1);
    }

    /*
    android WebView
     */
    private void initWebView() {
        webView.loadUrl("http://99android.xyz/m/");
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setAppCacheEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBarHorizontal.setProgress(newProgress);
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBarHorizontal.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String closeBanner = "javascript:(function(){document.getElementById('close_button').click();})()";
                view.loadUrl(closeBanner);
                progressBarHorizontal.setVisibility(View.GONE);
            }
        });
    }

    /*
    Init default notification
     */
    private void initDefaultNotification() {
        NotificationPresenter notificationPresenter = new NotificationPresenter(this, this);
        notificationPresenter.laodNotification();
    }

    @Override
    public void onNotificationFirstLoaded(List<Notifications> notifications) {
        insertNotification(notifications);
    }

    @Override
    public void onNotificationLoaded(List<Notifications> notifications) {

    }

    @Override
    public void onDataEmpty() {

    }

    /*
    Insert all first
     */
    private void insertNotification(List<Notifications> notifications) {
        Notifications notification;
        for (int i = 0; i < notifications.size(); i++) {
            notification = new Notifications();

            String title = notifications.get(i).getTitle();
            String body = Html.fromHtml(notifications.get(i).getBody()).toString();
            String date = notifications.get(i).getDate();
            String status_read = notifications.get(i).getStatus_read();

            notification.setTitle(title);
            notification.setBody(body);
            notification.setDate(date);
            notification.setStatus_read(status_read);
            notificationHelper.insert(notification);
            notificationHelper.getCount(textViewCountNotification);
        }
        AppPreferences.setFistInstall(this, false);
    }
}
