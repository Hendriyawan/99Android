package com.hdev.nineandroid.view;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.hdev.nineandroid.utils.AppPreferences;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
