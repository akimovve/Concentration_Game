package com.example.concentration;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtil {
    static void saveUserLevel(Context context, int level){
        saveValue(context, String.valueOf(level), "level");
    }

    public static int getUserLevel(Context context){
        String mDatabase = getValue(context, "level");
        if (mDatabase == null) mDatabase = "1";

        return Integer.valueOf(mDatabase);
    }

    private static void saveValue(Context context, String data, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, data);
        editor.apply();
    }

    private static String getValue(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getString(key, null);
    }
}
