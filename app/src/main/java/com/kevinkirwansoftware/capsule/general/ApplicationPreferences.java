package com.kevinkirwansoftware.capsule.general;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kevinkirwansoftware.capsule.TimePair;

import java.lang.reflect.Type;
import java.util.List;

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

    public static List<TimePair> getLatencyList(Context context, String tag){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(tag, null);
        Type type = new TypeToken<List<TimePair>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void setLatencyList(Context context, String tag, List<TimePair> timeList){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(timeList);
        editor.putString(tag, json);
        editor.apply();
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

    public static String getTempUnitSystemString(){
        if(isDegF()){
            return "imperial";
        } else {
            return "metric";
        }
    }

    public static String getTempUnitString(){
        if(isDegF()){
            return "F";
        } else {
            return "C";
        }
    }

    public static void remove(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
}
