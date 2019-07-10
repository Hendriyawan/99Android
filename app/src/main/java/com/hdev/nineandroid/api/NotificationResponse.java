package com.hdev.nineandroid.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.hdev.nineandroid.db.model.Notifications;

import java.util.List;

public class NotificationResponse implements Parcelable {
    public static final Parcelable.Creator<NotificationResponse> CREATOR = new Parcelable.Creator<NotificationResponse>() {
        @Override
        public NotificationResponse createFromParcel(Parcel source) {
            return new NotificationResponse(source);
        }

        @Override
        public NotificationResponse[] newArray(int size) {
            return new NotificationResponse[size];
        }
    };
    private String message;
    private Boolean success;
    private List<Notifications> notification;

    public NotificationResponse() {
    }

    protected NotificationResponse(Parcel in) {
        this.message = in.readString();
        this.success = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.notification = in.createTypedArrayList(Notifications.CREATOR);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Notifications> getNotification() {
        return notification;
    }

    public void setNotification(List<Notifications> notification) {
        this.notification = notification;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeValue(this.success);
        dest.writeTypedList(this.notification);
    }
}
