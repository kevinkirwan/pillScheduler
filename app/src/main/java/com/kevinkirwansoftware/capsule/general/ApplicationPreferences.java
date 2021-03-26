package com.kevinkirwansoftware.capsule.general;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationPreferences {
    private static String SHARED_PREFERENCES = "shared_preferences";
    private static String IS_24_HOUR = "is_24_hour";
    private static boolean is24Hour;

    public static void savePreferenceData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_24_HOUR, is24Hour);
        editor.apply();
    }

    public static void loadPreferenceData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        is24Hour = sharedPreferences.getBoolean(IS_24_HOUR, false);
    }

    public static boolean is24Hour(){
        return is24Hour;
    }

    public static void setIs24Hour(boolean is24Hour){
        ApplicationPreferences.is24Hour = is24Hour;
    }
}
