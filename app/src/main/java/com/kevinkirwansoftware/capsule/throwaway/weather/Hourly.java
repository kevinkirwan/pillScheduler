package com.kevinkirwansoftware.capsule.throwaway.weather;

import java.util.List;

public class Hourly {
    public int dt;
    public double temp;
    public double feels_like;
    public int pressure;
    public int humidity;
    public double dew_point;
    public double uvi;
    public int clouds;
    public int visibility;
    public double wind_speed;
    public int wind_deg;
    public double wind_gust;
    public List<Weather> weather;
    public double pop;
    public Rain rain;

    public Hourly(int dt, double temp, double feels_like, int pressure, int humidity, double dew_point, double uvi, int clouds, int visibility, double wind_speed, int wind_deg, double wind_gust, List<Weather> weather, double pop, Rain rain) {
        this.dt = dt;
        this.temp = temp;
        this.feels_like = feels_like;
        this.pressure = pressure;
        this.humidity = humidity;
        this.dew_point = dew_point;
        this.uvi = uvi;
        this.clouds = clouds;
        this.visibility = visibility;
        this.wind_speed = wind_speed;
        this.wind_deg = wind_deg;
        this.wind_gust = wind_gust;
        this.weather = weather;
        this.pop = pop;
        this.rain = rain;
    }

    public int getDt() {
        return dt;
    }

    public double getTemp() {
        return temp;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getDew_point() {
        return dew_point;
    }

    public double getUvi() {
        return uvi;
    }

    public int getClouds() {
        return clouds;
    }

    public int getVisibility() {
        return visibility;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public int getWind_deg() {
        return wind_deg;
    }

    public double getWind_gust() {
        return wind_gust;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public double getPop() {
        return pop;
    }

    public Rain getRain() {
        return rain;
    }
}
