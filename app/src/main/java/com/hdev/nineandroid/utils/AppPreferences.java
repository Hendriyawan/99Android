package com.hdev.nineandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreferences {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void setFistInstall(Context context, boolean firstInstall) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putBoolean("is_first_install", firstInstall);
        editor.apply();
    }

    public static boolean getFirstInstall(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean("is_first_install", true);
    }
}
