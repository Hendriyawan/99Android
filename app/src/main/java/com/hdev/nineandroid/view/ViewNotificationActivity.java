package com.hdev.nineandroid.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.hdev.nineandroid.R;
import com.hdev.nineandroid.db.helper.NotificationHelper;
import com.hdev.nineandroid.db.model.Notifications;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.biubiubiu.justifytext.library.JustifyTextView;

public class ViewNotificationActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_view_title)
    TextView textViewTitle;
    @BindView(R.id.text_view_body)
    JustifyTextView textViewBody;
    @BindView(R.id.text_view_date)
    TextView textViewDate;
    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notification);
        ButterKnife.bind(this);

        initToolbar();
        initView();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    /*
    Init Toolbar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /*
    Init View
     */
    private void initView() {
        Notifications notifications = getIntent().getParcelableExtra("parcelable_notification");

        textViewTitle.setText(String.format(getResources().getString(R.string.text_title), notifications.getTitle()));
        textViewBody.setText(String.format(getResources().getString(R.string.text_body), notifications.getBody()));
        textViewDate.setText(String.format(getResources().getString(R.string.text_date), notifications.getDate()));

        if (notifications.getStatus_read().equals("unread")) {
            Notifications notificationsReaded = new Notifications();
            notificationsReaded.setTitle(notifications.getTitle());
            notificationsReaded.setBody(notifications.getBody());
            notificationsReaded.setDate(notifications.getDate());
            notificationsReaded.setStatus_read("readed");
            notificationHelper = new NotificationHelper(this);
            notificationHelper.open();
            notificationHelper.update(notificationsReaded, String.valueOf(notifications.getId()));
        }
    }
}
