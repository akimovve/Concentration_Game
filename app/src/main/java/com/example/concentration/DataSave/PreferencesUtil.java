package com.example.concentration.DataSave;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtil {
    public static void saveUserLevel(Context context, int level){
        saveValue(context, String.valueOf(level), "level");
    }

    public static int getUserLevel(Context context){
        String mDatabase = getValue(context, "level");
        if (mDatabase == null) mDatabase = "1";
        return Integer.parseInt(mDatabase);
    }

    public static void saveComplexity(Context context, int colors){
        saveValue(context, String.valueOf(colors), "difLvl");
    }

    public static int getComplexity(Context context){
        String mDatabase = getValue(context, "difLvl");
        if (mDatabase == null) mDatabase = "0";
        return Integer.parseInt(mDatabase);
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
