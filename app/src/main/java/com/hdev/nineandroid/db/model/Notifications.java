package com.hdev.nineandroid.db.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Notifications implements Parcelable {
    public static final Parcelable.Creator<Notifications> CREATOR = new Parcelable.Creator<Notifications>() {
        @Override
        public Notifications createFromParcel(Parcel source) {
            return new Notifications(source);
        }

        @Override
        public Notifications[] newArray(int size) {
            return new Notifications[size];
        }
    };
    private int id;
    private String title;
    private String body;
    private String date;
    private String status_read;

    public Notifications() {
    }

    protected Notifications(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.body = in.readString();
        this.date = in.readString();
        this.status_read = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus_read() {
        return status_read;
    }

    public void setStatus_read(String status_read) {
        this.status_read = status_read;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.body);
        dest.writeString(this.date);
        dest.writeString(this.status_read);
    }
}
