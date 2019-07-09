package com.hdev.nineandroid.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hdev.nineandroid.R;
import com.hdev.nineandroid.adapter.NotificationAdapter;
import com.hdev.nineandroid.db.helper.NotificationHelper;
import com.hdev.nineandroid.db.model.Notifications;
import com.hdev.nineandroid.interfaces.NotificationView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationHistoryActivity extends AppCompatActivity implements NotificationView, NotificationAdapter.OnNotificationClick {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_notifications)
    RecyclerView recyclerViewNotifications;
    @BindView(R.id.text_view_empty_notification)
    TextView textViewEmptyNotification;
    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_history);
        ButterKnife.bind(this);

        initialize();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        notificationHelper.getAllNotification(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNotificationLoaded(List<Notifications> notifications) {
        initReyclerView(notifications);
    }

    @Override
    public void onDataEmpty() {
        recyclerViewNotifications.setVisibility(View.GONE);
        textViewEmptyNotification.setVisibility(View.VISIBLE);
    }


    /*
    Initialize
     */
    private void initialize(){
        initToolbar();
        initToolbar();
        notificationHelper = new NotificationHelper(this);
        notificationHelper.open();
        notificationHelper.getAllNotification(this);
    }

    /*
   Init Toolbar
    */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Pemberitahuan");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /*
    init RecyclerView
     */
    private void initReyclerView(List<Notifications> notifications) {
        NotificationAdapter adapter = new NotificationAdapter(this, notifications, this);
        recyclerViewNotifications.setAdapter(adapter);
    }

    @Override
    public void onClick(Notifications notifications) {
        Intent viewNotification = new Intent(this, ViewNotification.class);
        viewNotification.putExtra("parcelable_notification", notifications);
        startActivity(viewNotification);

    }
}
