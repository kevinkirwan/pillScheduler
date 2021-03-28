package com.kevinkirwansoftware.capsule.general;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationPreferences {
    private String TAG = "ApplicationPreferences.java";
    private static String SHARED_PREFERENCES = "shared_preferences";

    private static String PREF_IS_24_HOUR = "is_24_hour";
    private static String PREF_IS_AMPM = "is_ampm";
    private static String PREF_IS_F_DEG = "is_f_deg";
    private static boolean is24Hour, isAmpm, isDegF;

    public static void savePreferenceData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_IS_24_HOUR, is24Hour);
        editor.putBoolean(PREF_IS_AMPM, isAmpm);
        editor.putBoolean(PREF_IS_F_DEG, isDegF);
        editor.apply();
    }

    public static void loadPreferenceData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        is24Hour = sharedPreferences.getBoolean(PREF_IS_24_HOUR, false);
        isAmpm = sharedPreferences.getBoolean(PREF_IS_AMPM, true);
        isDegF = sharedPreferences.getBoolean(PREF_IS_F_DEG, true);
    }

    public static boolean is24Hour(){
        return is24Hour;
    }

    public static void setIs24Hour(boolean is24Hour){
        ApplicationPreferences.is24Hour = is24Hour;
    }

    public static boolean isAmpm(){ return isAmpm;}

    public static void setIsAmpm(boolean isAmpm){ ApplicationPreferences.isAmpm= isAmpm;}

    public static boolean isDegF(){ return isDegF;}

    public static void setIsDegF(boolean isDegF){ ApplicationPreferences.isDegF= isDegF;}
}
