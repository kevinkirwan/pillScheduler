package com.kevinkirwansoftware.capsule.throwaway.weather;

import android.database.Cursor;

import com.kevinkirwansoftware.capsule.WeatherData;

import org.json.JSONObject;

import java.util.List;

public class WeatherResponse {
    public int lat;
    public int lon;
    public String timezone;
    public int timezone_offset;
    public Current current;
    //public List<Minutely> minutely;
    //public List<Hourly> hourly;
    //public List<Daily> daily;
    //public List<Alert> alerts;

    public WeatherResponse(int lat, int lon, String timezone, int timezone_offset, Current current
            //, List<Minutely> minutely, List<Hourly> hourly, List<Daily> daily, List<Alert> alerts
                           ) {
        this.lat = lat;
        this.lon = lon;
        this.timezone = timezone;
        this.timezone_offset = timezone_offset;
        this.current = current;
        /*
        this.minutely = minutely;
        this.hourly = hourly;
        this.daily = daily;
        this.alerts = alerts;

         */
    }

    public int getLat() {
        return lat;
    }

    public int getLon() {
        return lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public int getTimezone_offset() {
        return timezone_offset;
    }

    public Current getCurrent() {
        return current;
    }
    /*

    public List<Minutely> getMinutely() {
        return minutely;
    }

    public List<Hourly> getHourly() {
        return hourly;
    }

    public List<Daily> getDaily() {
        return daily;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

     */
}
